package com.example.fetchcodingexercise

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fetchcodingexercise.ui.theme.FetchCodingExerciseTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FetchCodingExerciseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EntryList()
                }
            }
        }
    }
}

fun getEntries(entryList: MutableList<Entry>, context: Context){
    val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPI = retrofit.create(RetrofitAPI::class.java)
    val call: Call<ArrayList<Entry>> = retrofitAPI.getEntries()

    call!!.enqueue(object: Callback<ArrayList<Entry>> {
        override fun onResponse(
            call: Call<ArrayList<Entry>>,
            response: Response<ArrayList<Entry>>
        ) {
            if (response.isSuccessful){
                var entries: ArrayList<Entry> = ArrayList()
                entries = response.body()!!

                for(i in 0 until entries.size){
                    var entry = entries.get(i)
                    if (!entry.name.isNullOrEmpty()){
                        entryList.add(entry)
                    }
                }
                //Sort by listId first. Then by name.
                entries.sortedWith(compareBy({it.listId}, {it.name}))
            }
        }

        override fun onFailure(call: Call<ArrayList<Entry>>, t: Throwable) {
            Toast.makeText(
                context,
                "Fail to get the data..",
                Toast.LENGTH_SHORT
            )   .show()
        }
    })
}

@Preview(showBackground = true)
@Composable
fun EntryListPreview(){
    EntryList()
}

@Composable
fun EntryList(){
    val context = LocalContext.current
    var entries = remember { mutableStateListOf<Entry>() }
//    getEntries(entries, context)
//
    val testEntry = listOf<Entry>(
        Entry(id = "276", listId = 1, name = "Item 276"),
        Entry(id = "808", listId = 1, name = "Item 808"),
        Entry(id = "648", listId = 4, name = "Item 684")
    )
    LazyColumn(){
        items(testEntry) {entry ->
            Row(modifier = Modifier.padding(
                horizontal= 24.dp,
                vertical = 12.dp
            )
                .fillMaxWidth(),
                Arrangement.SpaceBetween){
                Text(text = "Name: ${entry.name}", Modifier.weight(1f))
                Text(text = "Id: ${entry.listId.toString()}")
            }
            Divider()
        }
    }

}


