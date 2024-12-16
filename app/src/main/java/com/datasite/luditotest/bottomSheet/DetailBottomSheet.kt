package com.datasite.luditotest.bottomSheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.datasite.luditotest.R
import com.datasite.luditotest.viewModel.DataTransferVM
import com.yandex.mapkit.search.SuggestItem
import kotlinx.coroutines.launch
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBottomSheet(
    showBottomSheet :MutableState<Boolean>,
    onClick: () -> Unit,
    onClickClose: () -> Unit
) : MutableState<Boolean> {

   val dataTransferVM : DataTransferVM = viewModel()

    val modalBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    var randomNumber by remember { mutableStateOf(0) }

    ModalBottomSheet(

        onDismissRequest = {

        coroutineScope.launch {
            showBottomSheet.value = false
        }
    },
        modifier = Modifier
            .imePadding()
            .statusBarsPadding(),
        sheetState = modalBottomSheetState,
        containerColor = colorResource(id = R.color.white),
        dragHandle = { BottomSheetDefaults.DragHandle() },
        content = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.white))
            ) {

                Row (modifier = Modifier.fillMaxWidth()){
                    Text(
                        text = dataTransferVM.title.value,
                        modifier = Modifier.weight(1f),
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.black),
                        maxLines = 1,
                        lineHeight = 20.sp,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.W700
                    )
                    Icon(painter = painterResource(id = R.drawable.carbon_close_filled),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                onClickClose()
                            },
                        tint = colorResource(id = R.color.gry2),
                        contentDescription = "close")
                }

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = dataTransferVM.subTitle.value,
                    modifier = Modifier,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.gry2),
                    maxLines = 1,
                    lineHeight = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.W700
                )

                Spacer(modifier = Modifier.size(12.dp))

                LaunchedEffect(Unit){
                    randomNumber = Random.nextInt(100, 5001)
                }

                val rating = randomNumber / 1000

                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (i in 1..5) {

                        if (i <= rating) {
                            Image(
                                painter = painterResource(id = R.drawable.mingcute_star_fill),
                                modifier = Modifier.size(24.dp),
                                contentDescription = "Filled Star"
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.mingcute_star_empty),
                                modifier = Modifier.size(24.dp),
                                contentDescription = "Empty Star"
                            )
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                    }

                    Spacer(modifier = Modifier.size(6.dp))

                    Text(
                        text = "оценок $randomNumber",
                        modifier = Modifier,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.gry2),
                        maxLines = 1,
                        lineHeight = 20.sp,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.W600
                    )
                }





                Spacer(modifier = Modifier.size(12.dp))

                Button(modifier = Modifier
                    .background(colorResource(id = R.color.white))
                    .height(43.dp)
                    .wrapContentWidth(),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.green)),
                    shape = RoundedCornerShape(100.dp),
                    onClick = onClick
                ) {

                    Text(
                        text = "Добавить в избранное",
                        modifier = Modifier,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.white),
                        maxLines = 1,
                        lineHeight = 20.sp,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.W700
                    )
                }
                Spacer(modifier = Modifier.size(40.dp))
            }
        }
    )
    return showBottomSheet
}

