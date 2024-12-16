
package com.datasite.luditotest.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.datasite.luditotest.R


//@Preview(showBackground = true)
@Composable
fun dialogCompose(
    name : MutableState<String>,
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
                    text = "Добавить адрес в избранное",
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    color = colorResource(id = R.color.black),
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W700,
                )

                Spacer(modifier = Modifier.size(20.dp))


                TextField(
                    modifier = Modifier
                        .height(48.dp)
                        .imePadding()
                        .fillMaxWidth()
                        .background(
                            color = colorResource(id = R.color.white),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    value = name.value,
                    onValueChange = { newText -> name.value = newText },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.edit_pen_icon),
                            tint = colorResource(id = R.color.black),
                            contentDescription = "pen"
                        )
                    },
                    textStyle = TextStyle(
                        color = colorResource(id = R.color.black),
                        fontWeight = FontWeight.W700,
                        fontSize = 16.sp
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = colorResource(id = R.color.black),
                        focusedTextColor = colorResource(id = R.color.black),
                        unfocusedLeadingIconColor = colorResource(id = R.color.white),
                        focusedLeadingIconColor = colorResource(id = R.color.white),
                        focusedBorderColor = colorResource(id = R.color.white2),
                        unfocusedBorderColor = colorResource(id = R.color.white2),
                    )
                )


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
                        text = "Подтвердить",
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
