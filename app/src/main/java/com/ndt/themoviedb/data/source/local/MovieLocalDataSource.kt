package com.ndt.themoviedb.data.source.local

import com.ndt.themoviedb.R
import com.ndt.themoviedb.data.model.Category
import com.ndt.themoviedb.data.model.Favorite
import com.ndt.themoviedb.data.source.MovieDataSource
import com.ndt.themoviedb.data.source.local.base.LocalAsyncTask
import com.ndt.themoviedb.data.source.local.base.LocalDataHandler
import com.ndt.themoviedb.data.source.local.dao.FavoritesDao
import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback

class MovieLocalDataSource(private val favoritesDao: FavoritesDao) : MovieDataSource.Local {

    override fun getCategories(listener: OnDataLoadedCallback<List<Category>>) {
        listener.onSuccess(
            listOf(
                Category(Category.CategoryEntry.NOW_PLAYING, R.drawable.now_playing),
                Category(Category.CategoryEntry.UPCOMING, R.drawable.upcoming),
                Category(Category.CategoryEntry.TOP_RATE, R.drawable.toprated),
                Category(Category.CategoryEntry.POPULAR, R.drawable.popular)
            )
        )
    }

    override fun getFavorites(listener: OnDataLoadedCallback<MutableList<Favorite>>) {
        LocalAsyncTask(object : LocalDataHandler<String, MutableList<Favorite>> {
            override fun execute(params: String): MutableList<Favorite> =
                favoritesDao.getAllFavorites()
        }, listener).execute("")
    }

    override fun addFavorite(favorite: Favorite, listener: OnDataLoadedCallback<Boolean>) {
        LocalAsyncTask(object : LocalDataHandler<Favorite, Boolean> {
            override fun execute(params: Favorite): Boolean = favoritesDao.addFavorite(params)
        }, listener).execute(favorite)
    }

    override fun deleteFavorite(movieID: String, listener: OnDataLoadedCallback<Boolean>) {
        LocalAsyncTask(object : LocalDataHandler<String, Boolean> {
            override fun execute(params: String): Boolean = favoritesDao.deleteFavorite(params)
        }, listener).execute(movieID)
    }

    override fun findFavoriteId(movieID: String, listener: OnDataLoadedCallback<Boolean>) {
        LocalAsyncTask(object : LocalDataHandler<String, Boolean> {
            override fun execute(params: String): Boolean = favoritesDao.findFavorite(params)
        }, listener).execute(movieID)
    }

    companion object {
        private var instance: MovieLocalDataSource? = null
        fun getInstance(favoritesDao: FavoritesDao) =
            instance ?: MovieLocalDataSource(favoritesDao).also { instance = it }
    }
}
