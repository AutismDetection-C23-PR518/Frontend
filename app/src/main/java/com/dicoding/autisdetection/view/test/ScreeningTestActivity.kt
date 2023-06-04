package com.dicoding.autisdetection.view.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dicoding.autisdetection.databinding.ActivityScreeningTestBinding
import com.dicoding.autisdetection.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ScreeningTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScreeningTestBinding
    private lateinit var interpreter: Interpreter

    private val questions: List<String> = listOf(
        "Does your child look at you when you call his/her name?",
        "How easy is it for you to get eye contact with your child?",
        "Does your child point to indicate that s/he wants something?",
        "Does your child point to share interest with you?",
        "Does your child pretend?",
        "Does your child follow where you’re looking?",
        "If you or someone else in the family is visibly upset, does your child show signs of wanting to comfort them?",
        "Would you describe your child’s first words as:",
        "Does your child use simple gestures?",
        "Does your child stare at nothing with no apparent purpose?"
    )

    private val answers: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreeningTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Load the TFLite model
        val modelFile = FileUtil.loadMappedFile(this, "model.tflite")
        val options = Interpreter.Options()
        interpreter = Interpreter(modelFile, options)

        // Process answers
        val totalScore = processAnswers()

        // Show the total score
        val totalScoreInt = totalScore.toInt() // Assuming the total score ranges from 0 to 10
        binding.skorTextview.text = "Skor: $totalScoreInt"
        Log.d("Result", "Result: $totalScoreInt")

        setQuestion()

        binding.nextButton.setOnClickListener {
            val answer = getSelectedAnswer()
            if (answer != null) {
                answers.add(answer)
                setQuestion()
            } else {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
            }
        }

//        binding.submitButton.setOnClickListener {
//            val answer = getSelectedAnswer()
//            if (answer != null) {
//                answers.add(answer)
//            }
//            displayResult()
//        }

        binding.answerRadiogroup.setOnCheckedChangeListener { _, _ ->
            binding.nextButton.isEnabled = true
        }

    }

    private fun setQuestion() {
        val currentQuestionIndex = answers.size
        if (currentQuestionIndex < questions.size) {
            binding.questionTextview.text = questions[currentQuestionIndex]
            binding.answerRadiogroup.clearCheck()
            binding.nextButton.isEnabled = false
        } else {
            displayResult()
        }
    }

    private fun getSelectedAnswer(): String? {
        val checkedId = binding.answerRadiogroup.checkedRadioButtonId
        return when (checkedId) {
            binding.answer1Radiobutton.id -> "A"
            binding.answer2Radiobutton.id -> "B"
            binding.answer3Radiobutton.id -> "C"
            binding.answer4Radiobutton.id -> "D"
            binding.answer5Radiobutton.id -> "E"
            else -> null
        }
    }



    private fun processAnswers(): Float {
        val model = Model.newInstance(this)

        // Convert answers to a float array
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 10), DataType.FLOAT32)
        val pointArray = FloatArray(10) // Array 1 dimensi untuk input point
        var totalScore = 0 // Total score

        for (i in answers.indices) {
            val answer = answers[i]
            val point =
                if (i < 9 && (answer == "C" || answer == "D" || answer == "E") || (i == 9 && (answer == "A" || answer == "B" || answer == "C"))) 1 else 0
            pointArray[i] = point.toFloat()
            totalScore += point

            // Log jawaban dan poin
            Log.d("Answer $i", "Answer: $answer, Point: $point")
        }

        val byteBuffer = ByteBuffer.allocateDirect(pointArray.size * 4) // 4 bytes per float
        byteBuffer.order(ByteOrder.nativeOrder())
        for (value in pointArray) {
            byteBuffer.putFloat(value)
        }
        byteBuffer.rewind()

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        val result = outputFeature0.floatArray[0]

        // Releases model resources if no longer used.
        model.close()

        return result
    }

    private fun displayResult() {
        val result = processAnswers()

        // Show the result as a toast message
        val resultText = if (result >= 0.5f) {
            "Your child is at risk of autis"
        } else {
            "Your child is not at risk of autism"
        }
        Toast.makeText(this, resultText, Toast.LENGTH_LONG).show()

        // Show the total score
        val totalScore = (result * 10).toInt() // Assuming the total score ranges from 0 to 10
        binding.skorTextview.text = "Skor: $totalScore"
        Log.d("Result", "Result: $result")

        // Hide the question and "Next" button
        binding.questionTextview.visibility = View.GONE
        binding.answerRadiogroup.visibility = View.GONE
        binding.nextButton.visibility = View.GONE

        // Show the total score and "Submit" button
        binding.skorTextview.visibility = View.VISIBLE
//        binding.submitButton.visibility = View.VISIBLE
    }
}