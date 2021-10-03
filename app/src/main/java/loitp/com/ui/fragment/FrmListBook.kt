package loitp.com.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.core.utilities.LActivityUtil
import com.core.utilities.LStoreUtil
import com.function.pump.download.Pump
import com.function.pump.download.core.DownloadListener
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.frm_list_book_favourite.*
import loitp.com.R
import loitp.com.adapter.AdapterListBook
import loitp.com.db.SQLiteHelper
import loitp.com.model.Book
import loitp.com.model.BookFavourite
import loitp.com.service.AsyncTaskParseJSON
import loitp.com.ui.activity.ListChapActivity
import loitp.com.util.Const
import loitp.com.util.Help
import java.io.File
import java.util.*

@LogTag("loitppFrm0Quote")
class FrmListBook : BaseFragment() {
    private val listBook: MutableList<Book> = ArrayList()
    private var mUrlDataGGDrive: String? = null
    private var fileName: String? = null
    private var adapterListBook: AdapterListBook? = null
    private var asyncTaskParseJSON: AsyncTaskParseJSON? = null

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_list_book_favourite
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onDestroyView() {
        asyncTaskParseJSON?.cancel(true)
        super.onDestroyView()
    }

    private fun setupViews() {
//        try {
//            arguments?.let {
//                mUrlDataGGDrive =
//                    "https://drive.google.com/uc?export=download&id=" + it.getString("mUrlDataGGDrive")
//                fileName = "/" + it.getString("fileName") + ".json"
//            }
//        } catch (e: Exception) {
//            mUrlDataGGDrive =
//                "https://drive.google.com/uc?export=download&id=0B0-bfr9v36LUN1VkM2JVV0k4WDQ"
//            fileName = "/databook.json"
//        }
        arguments?.let {
            mUrlDataGGDrive =
                "https://drive.google.com/uc?export=download&id=" + it.getString("mUrlDataGGDrive")
            fileName = "/" + it.getString("fileName") + ".json"
        }

        btRefresh.setSafeOnClickListener {
            btRefresh.isEnabled = false
            listBook.clear()
            adapterListBook?.notifyDataSetChanged()
            downloadDataFromGGDrive()
        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapterListBook?.let { a ->
                    a.filter?.filter(s)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
        adapterListBook = AdapterListBook(
            mContext = requireContext(),
            listBook = listBook,
            onClickRoot = { book ->
                Help.saveBooleanToData(Const.READ_FROM_FAVOURITE, false)
                val intent = Intent(activity, ListChapActivity::class.java)
                intent.putExtra("mUrl", book.url)
                intent.putExtra("mTitle", book.tit)
                intent.putExtra("imgThumbnail", book.thumb)
                startActivity(intent)
                LActivityUtil.tranIn(context)
            },
            onClickAddFavourite = { book ->
                showDlgAddBook(book)
            }
        )
        gridView.adapter = adapterListBook

        logD("fileName $fileName")
        logD("mUrlDataGGDrive $mUrlDataGGDrive")
        fileName?.let {
            val path = LStoreUtil.getFolderPath(folderName = it)
            logD("path $path")
            val file = File(path)
            if (file.exists()) {
                parseJson()
            } else {
                downloadDataFromGGDrive()
            }
        }
    }

    private fun downloadDataFromGGDrive() {
        tvMsg.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        logD("downloadDataFromGGDrive fileName $fileName")
        Pump.newRequestToDownload(mUrlDataGGDrive, Const.FOLDER)
            .listener(object : DownloadListener() {

                override fun onProgress(progress: Int) {
                    logD("onProgress progress $progress")
                }

                override fun onSuccess() {
                    val filePath = downloadInfo.filePath
                    showShortInformation("Download Finished $filePath")

                    showShortWarning(getString(R.string.da_cap_nhat_sach_moi_thanh_cong))
                    parseJson()
                }

                override fun onFailed() {
                    showShortError("Download failed")
                    btRefresh.isEnabled = true
                    progressBar.visibility = View.GONE
                    tvMsg.visibility = View.VISIBLE
                }
            })
            .forceReDownload(true)
            .threadNum(3)
            .setRetry(3, 200)
            .submit()
    }

    private fun parseJson() {
        logD("parseJson")
        fileName?.let {
            asyncTaskParseJSON?.cancel(true)
            asyncTaskParseJSON = AsyncTaskParseJSON(
                mFileName = it,
                onPreExecute = {
                    logD("onPreExecute")
                    progressBar.visibility = View.VISIBLE
                },
                onSuccess = { list ->
                    logD("onSuccess")
                    btRefresh.isEnabled = true
                    progressBar.visibility = View.GONE
                    listBook.addAll(list)
                    adapterListBook?.notifyDataSetChanged()
                },
                onFailed = {
                    logD("onFailed")
                    showShortWarning(getString(R.string.ds_truyen_trong))
                },
            )
            asyncTaskParseJSON?.execute()
        }
    }

    private fun showDlgAddBook(book: Book) {
        fun addBookToListFavourite() {
            val sqLiteHelper = SQLiteHelper()
            val bookFavourite = BookFavourite()
            bookFavourite.poswv = 0
            bookFavourite.url = book.url
            bookFavourite.thumb = book.thumb
            bookFavourite.tit = book.tit
            bookFavourite.aut = book.aut
            bookFavourite.rq = book.rq
            val isExist = sqLiteHelper.addBook(bookFavourite)
            if (isExist) {
                showShortInformation(
                    msg = getString(R.string.add_book_to_list_favourite_failed) + "\n" + book.tit,
                    isTopAnchor = true
                )
            } else {
                showShortInformation(
                    msg = getString(R.string.add_book_to_list_favourite_ok) + "\n" + book.tit,
                    isTopAnchor = true
                )
            }
        }

        showBottomSheetOptionFragment(
            title = getString(R.string.warning_vn),
            message = getString(R.string.ask_add_book),
            textButton1 = getString(R.string.yes),
            textButton2 = getString(R.string.no),
            onClickButton1 = {
                addBookToListFavourite()
            },
            onClickButton2 = {
                //do nothing
            }
        )
    }
}