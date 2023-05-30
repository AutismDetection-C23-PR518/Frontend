package com.dicoding.autisdetection.screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.autisdetection.R
import com.dicoding.autisdetection.auth.LoginActivity
import com.dicoding.autisdetection.databinding.ActivityIntroBinding
import com.dicoding.autisdetection.databinding.IntroAppDesignBinding
import com.dicoding.autisdetection.screen.IntroActivity.Companion.MAX_STEP
import com.google.android.material.tabs.TabLayoutMediator

class IntroActivity : Fragment() {
    private var _binding: ActivityIntroBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.intro_app_content, container, false)
        _binding = ActivityIntroBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //............................................................
        binding.viewPager2.adapter = AppIntroViewPager2Adapter()

        //............................................................
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
        }.attach()

        //............................................................
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if(position== MAX_STEP -1){
                    binding.btnNext.text                =   getString(R.string.intro_get_started)//"Get Started"
                    binding.btnNext.contentDescription  =   getString(R.string.intro_get_started)//"Get Started"
                }else{
                    binding.btnNext.text                  = getString(R.string.intro_next)//"Next"
                    binding.btnNext.contentDescription    = getString(R.string.intro_next)//"Next"
                }
            }
        })


        //............................................................
        binding.btnSkip.setOnClickListener {
            findNavController().navigateUp()
        }

        //............................................................
        binding.btnNext.setOnClickListener {
            if (binding.btnNext.text.toString() == getString(R.string.intro_get_started)) {
                findNavController().navigateUp()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

            } else {
                // to change current page - on click "Next BUTTON"
                val current = (binding.viewPager2.currentItem) + 1
                binding.viewPager2.currentItem = current

                // to update button text
                if (current >= MAX_STEP - 1) {
                    binding.btnNext.text = getString(R.string.intro_get_started) //"Get Started"
                    binding.btnNext.contentDescription = getString(R.string.intro_get_started) //"Get Started"
                } else {
                    binding.btnNext.text = getString(R.string.intro_next) //"Next"
                    binding.btnNext.contentDescription = getString(R.string.intro_next) //"Next"
                }
            }
        }




    }
    companion object {
        const val MAX_STEP = 3
    }
}
//...............................................................................


//................................................................................
class AppIntroViewPager2Adapter : RecyclerView.Adapter<PagerVH2>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH2 {
        return PagerVH2(
            IntroAppDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    //get the size of color array
    override fun getItemCount(): Int = MAX_STEP // Int.MAX_VALUE

    //binding the screen with view
    override fun onBindViewHolder(holder: PagerVH2, position: Int) = holder.itemView.run {

        with(holder) {
            if (position == 0) {
                bindingDesign.introTitle.text = "Autis Detection"
                bindingDesign.introDescription.text = "Aplikasi ini dapat mendeteksi apakah seseorang mengalami gangguan autis atau tidak"
                bindingDesign.introImage.setImageResource(R.drawable.onboarding)
            }
            if (position == 1) {
                bindingDesign.introTitle.text = "Login"
                bindingDesign.introDescription.text = "Login untuk menggunakan aplikasi"
                bindingDesign.introImage.setImageResource(R.drawable.login)
            }
            if (position == 2) {
                bindingDesign.introTitle.text = "Splash Scree"
                bindingDesign.introDescription.text = "Splash Screen"
                bindingDesign.introImage.setImageResource(R.drawable.splash)
            }
        }
    }
}
class PagerVH2(val bindingDesign: IntroAppDesignBinding) : RecyclerView.ViewHolder(bindingDesign.root)
