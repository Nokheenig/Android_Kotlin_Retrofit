package com.example.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.retrofit.models.Comment
import com.example.retrofit.models.JsonPlaceHolderApi
import com.example.retrofit.models.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var textViewResult: TextView
    private lateinit var jsonPlaceHolderApi: JsonPlaceHolderApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewResult = findViewById(R.id.text_view_result)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

        //getPosts()
        //getComments()
        createPost()
    }

    private fun getPosts(){
        val parameters = mutableMapOf<String,String>()
        parameters["userId"] = "1"
        parameters["_sort"] = "id"
        parameters["_order"] = "desc"
        val call = jsonPlaceHolderApi.getPosts(parameters)
        call.enqueue(object : Callback<MutableList<Post>> {
            override fun onResponse(
                call: Call<MutableList<Post>>,
                response: Response<MutableList<Post>>
            ) {
                if (response.isSuccessful) {
                    val posts = response.body()
                    for (post in posts!!) {
                        var content = ""
                        content += "ID: ${post.id}\n"
                        content += "User ID: ${post.userId}\n"
                        content += "Title: ${post.title}\n"
                        content += "Text: ${post.text}\n\n"

                        textViewResult.append(content)
                    }
                } else {
                    textViewResult.text = response.code().toString()
                }
            }

            override fun onFailure(call: Call<MutableList<Post>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.toString()}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getComments() {
        val call = jsonPlaceHolderApi.getComments("posts/2/comments") // for instance

        call.enqueue(object: Callback<MutableList<Comment>>{
            override fun onResponse(
                call: Call<MutableList<Comment>>,
                response: Response<MutableList<Comment>>
            ) {
                if (response.isSuccessful) {
                    val comments = response.body()

                    for (comment in comments!!) {
                        var content = ""

                        content += "ID: ${comment.id}\n"
                        content += "Post ID: ${comment.postId}\n"
                        content += "Name: ${comment.name}\n"
                        content += "Email: ${comment.email}\n"
                        content += "Text: ${comment.text}\n\n"

                        textViewResult.append(content)
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<Comment>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.toString()}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createPost() {
        val post = Post(20, "New title", "New text")

        val fields = mutableMapOf<String,String>()
        fields.put("userId", "66")
        fields.put("title", "This is a new title")
        fields.put("body", "This is a new text")

        val call = jsonPlaceHolderApi.createPost(fields)

        call.enqueue(object: Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
                    val postResponse = response.body()
                    var content = ""
                    content += "Code: ${response.code()}\n"
                    content += "ID: ${postResponse?.id}\n"
                    content += "User ID: ${postResponse?.userId}\n"
                    content += "Title: ${postResponse?.title}\n"
                    content += "Text: ${postResponse?.text}\n\n"

                    textViewResult.append(content)

                } else {
                    textViewResult.text = response.code().toString()
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.toString()}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}