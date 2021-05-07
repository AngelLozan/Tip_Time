package com.example.tiptime

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.calculateButton.setOnClickListener { calculateTip() }
        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(
                view,
                keyCode
            )
        }
    }

    //get the cost of the service. Save the text attribute of the EditText cost of service as a #.
    //convert it to a string because text of editable is considered and editable (not string)
    //save new variable as the string text converted to a decimal number (double)
    private fun calculateTip() {
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        val cost =
            stringInTextField.toDoubleOrNull() //toDoubleOrNull in case no amount entered (empty string)
        if (cost == null) { //checks if cost is null, returns and stops process so app doesn't crash.
            binding.tipResult.text =
                "" //this way,edge case if calculate, erase and calculate empty string, no old tip displayed to confuse.
            return
        }
        //Get tip percentage
        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }
        //Now calculate the tip since all variables needed are found/defined above
        var tip = tipPercentage * cost //use var not val since var is changeable (may need to round)
        val roundUp =
            binding.roundUpSwitch.isChecked //isChecked attribute in switch shows if on or off
        if (roundUp) { //Now, if switch on, just round up so use ceil() function.
            tip = ceil(tip) //don't need to import library if just using one function from it.
        }
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        //Set the text of tipResult text view element (locate with binding) to the reference (place)
        // where tip amount displays on screen, and display the above value stored as variable formatted correctly.
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}