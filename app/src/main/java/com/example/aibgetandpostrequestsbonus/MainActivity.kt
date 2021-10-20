package com.example.aibgetandpostrequestsbonus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL="https://dojo-recipes.herokuapp.com/"
class MainActivity : AppCompatActivity() {
    lateinit var EDT:EditText
    lateinit var BTNADD:Button
    lateinit var BTNSHOW: Button
    lateinit var TV:TextView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EDT=findViewById(R.id.edtname)
        BTNADD=findViewById(R.id.btnadd)
        BTNSHOW=findViewById(R.id.btnshow)
        TV=findViewById(R.id.tv)

        BTNSHOW.setOnClickListener {
            val retorfitBuilder= Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(APIInterface::class.java)

            val retrofitData =retorfitBuilder.getData()

            retrofitData.enqueue(object : Callback<List<MyDataItem>?> {
                override fun onResponse(
                    call: Call<List<MyDataItem>?>,
                    response: Response<List<MyDataItem>?>,
                ) {
                    val responseBody=response.body()!!


                    var stringToBePritined:String? = ""
                    for(myData in responseBody){
                        stringToBePritined = stringToBePritined+"\n" +myData.name

                    }

                    TV.text= stringToBePritined

                }

                override fun onFailure(call: Call<List<MyDataItem>?>, t: Throwable) {

                }
            })
        }

        BTNADD.setOnClickListener {
            var f = MyDataItem(EDT.text.toString())

            addSingleuser(f, onResult = {

                EDT.setText("")

                Toast.makeText(applicationContext, "Save Success!", Toast.LENGTH_SHORT).show();
            })
        }


    }

    private fun addSingleuser(f: MyDataItem, onResult: () -> Unit) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)



        if (apiInterface != null) {
            apiInterface.addUser(f).enqueue(object : Callback<MyDataItem> {
                override fun onResponse(
                    call: Call<MyDataItem>,
                    response: Response<MyDataItem>
                ) {

                    onResult()

                }

                override fun onFailure(call: Call<MyDataItem>, t: Throwable) {
                    onResult()
                    Toast.makeText(applicationContext, "Error!", Toast.LENGTH_SHORT).show();


                }
            })
        }

    }

}