package com.cognifygroup.vgold.activities

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cognifygroup.vgold.R
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

@SuppressLint("Registered")
class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = resources.getString(R.string.app_name)
    }

    fun printError(tag: String?, message: String?, e: Exception?) {
        if (DEBUG_ENABLE) {
            Log.e(tag, message, e)
        }
    }

    /* check email id is valid or not */
    fun isValidEmailId(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (!Pattern.matches(EMAIL_REGEX, text)) {
            editText.requestFocus()
            false
        } else {
            true
        }
    }

    companion object {
        private const val DEBUG_ENABLE = true
        private const val EMAIL_REGEX =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

        fun printLog(tag: String?, message: String?) {
            if (DEBUG_ENABLE) {
                Log.d(tag, message!!)
            }
        }

        /* show toast message to user */
        fun showToast(context: Context?, message: String?) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        //---Function to check network connection---//
        fun isNetworkAvailable(context: Context): Boolean {
            try {
                val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = cm.activeNetworkInfo
                if (networkInfo != null && networkInfo.isConnected) return true
            } catch (e: Exception) {
                e.printStackTrace()
            } catch (e: Error) {
                e.printStackTrace()
            }
            return false
        }

        fun showSnackBar(view: View?, message: String?) {
            Snackbar.make(view!!, message!!, Snackbar.LENGTH_LONG).show()
        }

        fun hideKeyboard(context: Context, view: View?) {
            // Check if no view has focus:
            //View view = context.get
            if (view != null) {
                val inputManager =
                    context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }

        /*public static void createCartSessionRandomNo()
	{
		String cartSession="";
		char[] charset = "0123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			char c = charset[random.nextInt(charset.length)];
			sb.append(c);
		}
		cartSession = sb.toString();
		Log.e("overSessionRandomNumber=",""+cartSession);
		VegetableApplication.onSetRandomCartSessionValue(cartSession);
	}*/
        fun isOkToSave(data: String?): Boolean {
            return if (data != null && data != "" && !data.equals(
                    "Not Specified",
                    ignoreCase = true
                ) && !data.equals("null", ignoreCase = true)
            ) {
                true
            } else {
                false
            }
        }

        val date: String
            get() {
                val formatter = SimpleDateFormat("yyyy-MM-dd-HHmmss")
                val date = Date()
                return formatter.format(date)
            }
        val dateTime: String
            get() {
                val formatter = SimpleDateFormat("yyyyMMddHHmmss")
                val date = Date()
                return formatter.format(date)
            }

        fun convertDateTimeFormat(date: String?, formatFrom: String?, formatTo: String?): String {
            val sdf = SimpleDateFormat(formatFrom)
            var convertedDate: Date? = null
            try {
                convertedDate = sdf.parse(date)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            val formatter = SimpleDateFormat(formatTo)
            return formatter.format(convertedDate)
        }
    }
}

