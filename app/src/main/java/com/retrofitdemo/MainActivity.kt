package com.retrofitdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.example.retrofitdemo.databinding.ActivityMainBinding
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    //Instance of retrofit service object that we will use in our functions
    private lateinit var retService: AlbumService
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retService = RetrofitInstance
            .getRetrofitInstance()
            .create(AlbumService::class.java)

        //getRequestWithPathParameters()
        getRequestWithQueryParameters()
        //uploadNewAlbum()

    }

    private fun getRequestWithQueryParameters(){

        //We are creating a live data object , we are using liveData{ }
        // builder to emit an asynchronous retrofit object that gets data(Albums with userId = 3)
        //from the Api Service
        val responseLiveData: LiveData<Response<Albums>> = liveData {
            val response = retService.getSortedAlbums(3)
            emit(response)
        }

        //Observing the objected we've created above , we can change and set the new
        // value to our text view
        responseLiveData.observe(this) {
            //Here we create this listIterator object so we can access to every element with
            // the While loop , hasNext() and next() functions
            val albumsList = it.body()?.listIterator()
            if (albumsList != null) {
                while (albumsList.hasNext()) {
                    val albumsItem = albumsList.next()
                    val result =
                        " " + "Album Title : ${albumsItem.title}" + "\n" +
                                " " + "Album id : ${albumsItem.id}" + "\n" +
                                " " + "User id : ${albumsItem.userId}" + "\n\n\n"
                    //With this append function we add every new albumsItem added to our text view
                    binding.textView.append(result)
                }
            }
        }
    }

    private fun getRequestWithPathParameters(){
        // path parameter example
        val pathResponse : LiveData<Response<AlbumsItem>> = liveData {
            val response = retService.getAlbum( 23)
            emit(response)
        }

        pathResponse.observe(this, Observer {
            val title = it.body()?.title
            Toast.makeText(applicationContext,title,Toast.LENGTH_LONG).show()
        })
    }

    private fun uploadNewAlbum() {

        val postResponse : LiveData<Response<AlbumsItem>> = liveData {
            val response = retService.uploadAlbum(AlbumsItem(0, "My title", 3))
            emit(response)
        }
        postResponse.observe(this, Observer {
            val receivedAlbumsItem = it.body()
            val result =
                " " + "Album Title : ${receivedAlbumsItem?.title}" + "\n" +
                        " " + "Album id : ${receivedAlbumsItem?.id}" + "\n" +
                        " " + "User id : ${receivedAlbumsItem?.userId}" + "\n\n\n"
            binding.textView.text = result
        })
    }


}