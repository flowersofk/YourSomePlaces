import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.flowersofk.yoursomeplaces.data.PlaceProduct
import com.flowersofk.yoursomeplaces.api.SearchPlaceService
import com.flowersofk.yoursomeplaces.api.getSearchPlace

class PlaceDataSource(
    private val page: Int,
    private val service: SearchPlaceService
) : PageKeyedDataSource<Int, PlaceProduct>() {

    val networkErrors: MutableLiveData<String> = MutableLiveData()
    val countData: MutableLiveData<Int> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PlaceProduct>) {

        getSearchPlace(service, page,
            { count, response ->
                countData.postValue(count)
                callback.onResult(response, null, page + 1) },
            { error ->  networkErrors.postValue(error)})
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PlaceProduct>) {

        getSearchPlace(service, params.key,
            { count, response ->
                countData.postValue(count)
                callback.onResult(response, params.key + 1) },
            { error ->  networkErrors.postValue(error)})

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PlaceProduct>) {
        TODO("Not yet implemented")
    }


}