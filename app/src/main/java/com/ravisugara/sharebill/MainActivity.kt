package com.ravisugara.sharebill

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList


class MainActivity : AppCompatActivity() {
    private var Path: String? = null
    private var templatePDF: TemplatePDF? = null
    private val header = arrayOf("Date/Time", "Transaction Type")
    var rp: String? = null

    // allow to less sequrity to sent mail
    //https://www.youtube.com/redirect?event=video_description&redir_token=QUFFLUhqbkktWmQweldvMHZ2M3ptMW9Vc1ZMZDZZT3ZVUXxBQ3Jtc0tsYzNQWENqY0tpVE5XQndiVmhkazVhV1RlSzZxZG03RWFDdHhqUUdFeUdSWkpXWTBtWTdQaVlVcExRajNVYjNGazFQeGNHTGZMMFpzalFvMDJ0d3hkQXh6SVVtRUdmQ3p2NFlxWlA0ak1uWkFtNnd4NA&q=https%3A%2F%2Fmyaccount.google.com%2Flesssecureapps%3Fpli%3D1
    //Gyhjrfgb@55
    //https://myaccount.google.com/lesssecureapps?pli=1&rapt=AEjHL4OuRI3ZW9yK29lS6vX77zANOY4Uzx9fERdUdvjwneKGhqUmVBhEzccX10VI8z2h0H7eo7PLIdRT0OsJfORDooCZKjwOFg
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissionRequest(this)
//        name = findViewById(R.id.user_name)
//        email = findViewById(R.id.user_email)
//        number = findViewById(R.id.user_phone)
//        address = findViewById(R.id.user_address)
//        sendMail = findViewById(R.id.send_email)
//        sendMessage = findViewById(R.id.send_message)


        send_email?.setOnClickListener {
            if (permissionRequest(this)){
                rp = user_email?.editText!!.text.toString().trim { it <= ' ' }
                if (!rp.isNullOrEmpty()) createPdf(1) else Toast.makeText(
                    this@MainActivity,
                    "Please Enter Email Id ",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                permissionRequest(this)
            }

        }
        send_message.setOnClickListener {
            if (permissionRequest(this)) {
                if (!contact_no.editText?.text.toString().trim().isNullOrEmpty())
                    createPdf(2)
                else
                    Toast.makeText(
                        this@MainActivity,
                        "Please Enter  Contact Number ",
                        Toast.LENGTH_SHORT
                    ).show()
            }
            else{
                permissionRequest(this)
            }
        }

    }

    private fun createPdf(i: Int) {
        templatePDF = TemplatePDF(applicationContext)
        templatePDF!!.openDocument()
        templatePDF!!.addTitle(
            "Company Name", """
     Posykart
     address
     """.trimIndent(),
            "Bill"
        )

        templatePDF!!.createTable(header, userDetail)
        templatePDF!!.closeDocument()
        Path = templatePDF!!.pdfFilePath

        if (i==2){
            sendWhatAppMessage()
        }else if (i==1){
            senEmail()
        }
//        val intent = Intent(this, ViewPdfCustomer::class.java)
//        intent.putExtra("cus", Path)
//        startActivity(intent)
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun sendWhatAppMessage() {
        var toNumber = "" // contains spaces.
      //  toNumber = toNumber.replace("+", "").replace(" ", "")
        toNumber = "91${contact_no.editText?.text.toString().trim()}"
        val uri = Uri.parse(Path)
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TEXT, "message")
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
        sendIntent.putExtra("jid", "$toNumber@s.whatsapp.net")
        sendIntent.setPackage("com.whatsapp")
        sendIntent.type = "*/*"
        startActivity(sendIntent)
    }
    private val userDetail: ArrayList<Array<String>>
        private get() {
            val rows = ArrayList<Array<String>>()
            rows.add(arrayOf(resources.getString(R.string.name), user_name.editText?.text.toString()))
            rows.add(arrayOf(resources.getString(R.string.mobile_number), user_phone.editText?.text.toString()))
            rows.add(arrayOf(resources.getString(R.string.email), user_email.editText?.text.toString()))
            rows.add(arrayOf(resources.getString(R.string.organization), org.editText?.text.toString()))
            rows.add(arrayOf(resources.getString(R.string.customer_name), customer_name.editText?.text.toString()))
            rows.add(arrayOf(resources.getString(R.string.address), user_address.editText?.text.toString()))
            rows.add(arrayOf(resources.getString(R.string.contact_no), contact_no.editText?.text.toString()))
            rows.add(arrayOf(resources.getString(R.string.plane), plane.editText?.text.toString()))
            rows.add(arrayOf(resources.getString(R.string.staring_date), staring_date.editText?.text.toString()))
            rows.add(arrayOf(resources.getString(R.string.complementary), complementary.editText?.text.toString()))
            rows.add(arrayOf(resources.getString(R.string.other), other.editText?.text.toString()))
            rows.add(arrayOf(resources.getString(R.string.amount), amount.editText?.text.toString()))
            rows.add(arrayOf(resources.getString(R.string.payment), "Cash"))
            rows.add(arrayOf(resources.getString(R.string.registration), registration.editText?.text.toString()))
            return rows
        }

    private fun myMessage() {


       /* val myNumber: String = number?.editText?.text.toString()
        val mMessage =
            """________________________________________________________
               | User Name :-  ${name!!.editText!!.text}               |
               |_______________________________________________________|
               | Mobile Number :- ${number!!.editText!!.text}          |
               |_______________________________________________________|
               | Email :-$rp                                           |
               |_______________________________________________________|
               | Address :- 126 mg rode                                |
               |_______________________________________________________|
                """.trimIndent() + address!!.editText!!.text
                .toString()
        if (myNumber == "" || mMessage == "") {
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            if (TextUtils.isDigitsOnly(myNumber)) {
                val smsManager: SmsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(myNumber, null, mMessage, null, null)
                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter the correct number", Toast.LENGTH_SHORT).show()
            }
        }*/
    }

    private fun senEmail() {
        val mEmail = rp
        val mSubject = "Test User"
        val javaMailAPI = JavaMailAPI(this, mEmail, mSubject, "mMessage",Path)
        javaMailAPI.execute()

        Toast.makeText(this, "Email Sent", Toast.LENGTH_SHORT).show()
    }
}