package com.example.citiesdistanceusinghilt.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.citiesdistanceusinghilt.R
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineExceptionHandler
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.core.component.getScopeName
import timber.log.Timber

abstract class BaseFragment : Fragment(), BaseView {
    override val rootView: ConstraintLayout?
        get() = requireView() as ConstraintLayout
    override val viewContext: Context?
        get() = context

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}

abstract class BaseActivity : AppCompatActivity(), BaseView {
    override val rootView: ConstraintLayout?
        get() = prepareView()

    private fun prepareView(): ConstraintLayout {
        val viewGroup = window.decorView.findViewById(android.R.id.content) as ViewGroup
        if (viewGroup !is ConstraintLayout) {
            viewGroup.children.forEach {
                if (it is ConstraintLayout) return it
            }
            throw IllegalStateException("RootView must be instance of ConstraintLayout")
        } else {
            return viewGroup
        }
    }

    override val viewContext: Context?
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}

interface BaseView {
    val rootView: ConstraintLayout?
    val viewContext: Context?
    fun setProgressIndicator(mustShow: Boolean) {
        rootView?.let {
            var loadingView = it.findViewById<View>(R.id.frameLayout_viewLoading)
            viewContext?.let { context ->
                loadingView = addLoadingView(loadingView, mustShow, context, it)
                loadingView?.visibility = if (mustShow) View.VISIBLE else View.GONE
            }
        }
    }

    fun addLoadingView(
        loadingView: View?, mustShow: Boolean, context: Context, it: ConstraintLayout
    ): View? {
        var loadingView1 = loadingView
        if (loadingView1 == null && mustShow) {
            loadingView1 =
                LayoutInflater.from(context).inflate(R.layout.view_loading, it, false)
            it.addView(loadingView1)
        }
        return loadingView1
    }

    fun showEmptyState(layoutResId: Int): View? {
        rootView?.let {
            viewContext?.let { context ->
                return prepareEmptyState(it, context, layoutResId)
            }
        }
        return null
    }

    fun prepareEmptyState(it: ConstraintLayout, context: Context, layoutResId: Int): View? {
        var emptyState =
            it.findViewById<View>(R.id.constraintLayout_viewDistanceEmptyState_rootView)
        if (emptyState == null) {
            emptyState = LayoutInflater.from(context).inflate(layoutResId, it, false)
            it.addView(emptyState)
        }
        emptyState.visibility = View.VISIBLE
        return emptyState
    }

    fun showSnackBar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        rootView?.let {
            Snackbar.make(it, message, duration).show()
        }
    }

    fun showToast(message: String) {
        rootView?.let {
            Toast.makeText(it.context, message, Toast.LENGTH_SHORT).show()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showError(baseException: BaseException) {
        viewContext?.let {
            when (baseException.type) {
                BaseException.Type.SIMPLE -> showSnackBar(
                    baseException.serverMessage ?: it.getString(baseException.userFriendlyMessage))

                BaseException.Type.AUTH -> showToast(baseException.serverMessage.toString())

                BaseException.Type.DIALOG -> TODO()
            }
        }
    }
}

abstract class BaseViewModel : ViewModel() {
    val compositeDisposable = CompositeDisposable()
    val progressDialogLiveData = MutableLiveData<Boolean>()
    val snackBarLiveData = MutableLiveData<Event<String>>()
    val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Timber.e("CoroutineExceptionHandler : ScopeName -> ${coroutineContext.getScopeName()} : ${throwable.message}")
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}