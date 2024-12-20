
package com.datasite.luditotest.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.datasite.luditotest.R


//@Preview(showBackground = true)
@Composable
fun dialogDeleteCompose(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {


    AlertDialog(
        modifier = Modifier
            .wrapContentSize(),
        onDismissRequest = { onDismissRequest() },
        containerColor = colorResource(id = R.color.white),
        confirmButton = {  },
        shape = RoundedCornerShape(16.dp),

        text = {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .background(colorResource(id = R.color.white)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier,
                    text = "Удалить все",
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    color = colorResource(id = R.color.black),
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W700,
                )

                Spacer(modifier = Modifier.size(20.dp))

                Text(
                    modifier = Modifier,
                    text = "Вы хотите удалить все списки?",
                    fontSize = 16.sp,
                    lineHeight = 19.sp,
                    color = colorResource(id = R.color.gry5),
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W700,
                )

                Spacer(modifier = Modifier.size(20.dp))

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {

                    Text(
                        modifier = Modifier
                        .clickable { onDismissRequest() },
                        textAlign = TextAlign.Center,
                        text = "Отмена",
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.red),
                        maxLines = 1,
                        fontWeight = FontWeight.W700
                    )

                    Text(
                        modifier = Modifier
                            .clickable { onConfirm()},
                        textAlign = TextAlign.Center,
                        text = "Удалить",
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.black),
                        maxLines = 1,
                        fontWeight = FontWeight.W700
                    )
                }
            }
        }
    )
}
