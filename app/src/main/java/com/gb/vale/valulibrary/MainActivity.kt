package com.gb.vale.valulibrary

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.gb.vale.uivalulibrary.label.UiTayEditBasic

class MainActivity : AppCompatActivity() {


    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var selected = findViewById<UiTayEditBasic>(R.id.edittest)
        var ctn = findViewById<ConstraintLayout>(R.id.crnaa)

        selected.setListOptionDropDawn(arrayListOf("opcion1","opcion2","opcion3","opcion4"))
        selected.setOnListClickTayEditListener(ctn,selected){

        }
    }

}



