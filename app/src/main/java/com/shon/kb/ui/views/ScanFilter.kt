package com.shon.kb.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shon.kb.data.ScanFilterBean

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanFilterTitleLayout(filterList:List<ScanFilterBean> = emptyList(),
                          onInputClick:()->Unit = {},
                          onBtnClick:()->Unit={}){
    Row (
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)){

        InputChip(modifier = Modifier
            .weight(1f)
            .height(50.dp), selected = false, onClick = onInputClick, label = {
            LazyRow{
                items(count = filterList.size){
                    Row {
                        Text(text = filterList[it].name,
                            fontSize = 14.sp,
                            color = Color.Gray)
                        Spacer(modifier = Modifier.width(5.dp))
                    }

                }
            }
        })
        Spacer(modifier = Modifier.width(5.dp))
        Button(modifier = Modifier.wrapContentWidth(), onClick = onBtnClick) {
            Text(text = "Scan")
        }
    }
}
@Preview
@Composable
fun ScanFilterTitleLayoutPreview(){
    ScanFilterTitleLayout()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFilterLayout(){
Column {
    Row {
        OutlinedTextField(value = "", onValueChange = {

        })
    }
}
}


@Preview
@Composable
fun EditFilterLayoutPreview(){
    EditFilterLayout()
}
