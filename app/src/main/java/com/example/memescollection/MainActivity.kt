package com.example.memescollection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.memescollection.view.fragments.FavoriteMemesList
import com.example.memescollection.view.fragments.MemesListFragment
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val title = findViewById<ComposeView>(R.id.cv_title)
        title.setContent {
            AppCompatTheme {
                Surface {
                    displayTitle()
                }
            }
        }

        val navigationBar = findViewById<BottomNavigationView>(R.id.btv_bottom_navigation)
        setCurrentFragment(MemesListFragment())

        navigationBar.setOnItemSelectedListener {
            when (it.itemId){
                R.id.home -> setCurrentFragment(MemesListFragment())
                R.id.favorites -> setCurrentFragment(FavoriteMemesList())
            }
            true
        }

    }

    private fun setCurrentFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_memes, fragment)
            .commit()
    }
}




@Composable
private fun displayTitle() {
    Text(
        text = stringResource(R.string.memes_collection),
        textAlign = TextAlign.Center,
        fontSize = 32.sp,
        fontFamily = FontFamily.Cursive,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray)
            .padding(top = 16.dp, bottom = 16.dp)
    )
}




