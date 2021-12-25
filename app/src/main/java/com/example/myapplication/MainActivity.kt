package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.bd.HabitDbTable
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.single_card.*

class MainActivity : AppCompatActivity() {

    //private lateinit var appBarConfiguration: AppBarConfiguration
    //private lateinit var binding: ActivityMainBinding
    /*
    private lateinit var tvDescription : TextView
    private lateinit var tvTitle : TextView
    private lateinit var ivIcon : ImageView
    private lateinit var rv :RecyclerView
    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //iv_icon.setImageResource(R.drawable.water)
        //tv_title.text = getString(R.string.drink_water)
        //tv_description = findViewById(R.id.tv_description)
        //tv_description.text = getString(R.string.tv_description)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)
        //rv.adapter = HabitsAdapter(getSampleHabits())
        rv.adapter = HabitsAdapter(HabitDbTable(this).readAllHabits())

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

       override fun onOptionsItemSelected(item: MenuItem): Boolean {
           // Handle action bar item clicks here. The action bar will
           // automatically handle clicks on the Home/Up button, so long
           // as you specify a parent activity in AndroidManifest.xml.
           if (item.itemId == R.id.add_habit){
               switchTo(CreateHabitActivity::class.java)
           }
           return true
       }

    private fun switchTo(c: Class<*>) {
        val intent = Intent(this, c)
        startActivity(intent)
    }
/*
       override fun onSupportNavigateUp(): Boolean {
           val navController = findNavController(R.id.nav_host_fragment_content_main)
           return navController.navigateUp(appBarConfiguration)
                   || super.onSupportNavigateUp()
       }
       */
}