package saintdev.kr.dsteamproject.views.activitys

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.EditText
import saintdev.kr.dsteamproject.R
import saintdev.kr.dsteamproject.views.fragments.MainMapFragment

class MainActivity : AppCompatActivity() {
    val registedFragment = arrayOf(
            MainMapFragment()
    )
    private lateinit var toolBar: Toolbar

//    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//        when (item.itemId) {
//            R.id.navigation_home -> {
//                message.setText(R.string.title_home)
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_dashboard -> {
//                message.setText(R.string.title_dashboard)
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_notifications -> {
//                message.setText(R.string.title_notifications)
//                return@OnNavigationItemSelectedListener true
//            }
//        }
//        false
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(0)      // 최초 홈 화면을 띄웁니다.

        this.toolBar = findViewById(R.id.main_toolbar)
        setSupportActionBar(this.toolBar)

//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun replaceFragment(idx: Int) {
        val fragmnTrans = supportFragmentManager.beginTransaction()
        fragmnTrans.replace(R.id.main_container, registedFragment[idx]).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        // 텍스트 컬러 변경
        (searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as EditText)
                .setTextColor(Color.WHITE)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }
}
