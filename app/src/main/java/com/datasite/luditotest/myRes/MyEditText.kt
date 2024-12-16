package com.datasite.luditotest.myRes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.datasite.luditotest.R

@Composable
fun MyEditText(
    colorBase: Int,
    text: String,
    placeHolder: String,
    isFocused: Boolean,
    isError: Boolean,
    onTextChanged: (String) -> Unit,
) {
    val isFocusedState = remember { mutableStateOf(isFocused) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Update the border color based on focus and error state
    val borderColor = when {
        isError -> R.color.red // Error color
        isFocusedState.value -> R.color.white  // Focused color
        else -> R.color.white  // Default color
    }

    val borderColorBase = when {
        isError -> R.color.red // Error color
        isFocusedState.value -> R.color.white2  // Focused color
        else -> R.color.white  // Default color
    }

    Box(modifier = Modifier.fillMaxWidth()
        .border(
        BorderStroke(1.dp, colorResource(id = borderColorBase)),
        shape = RoundedCornerShape(16.dp),
    )){
        TextField(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFocusedState.value = it.isFocused
                }
                .border(
                    BorderStroke(8.dp, colorResource(id = borderColor)),
                    shape = RoundedCornerShape(16.dp),
                )
                .height(64.dp)
                .background(
                    color = colorResource(id = colorBase),
                    shape = RoundedCornerShape(16.dp)
                ),
            value = text,
            onValueChange = { newText -> onTextChanged(newText) },
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = placeHolder,
                    textAlign = TextAlign.Start,
                    fontWeight = W700,
                    color = colorResource(id = R.color.black),
                    style = TextStyle(fontSize = 16.sp)
                )
            },
            maxLines = 1,
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.icon_search),
                    tint = if (isFocusedState.value) colorResource(id = R.color.black) else colorResource(
                        id = R.color.black
                    ),
                    contentDescription = "search icon"
                )
            },
            trailingIcon = {
                if (text.isNotEmpty()) { // Show clear icon only when there's text
                    Icon(
                        painter = painterResource(id = R.drawable.carbon_close_filled), // Add your clear icon resource here
                        contentDescription = "Clear text",
                        tint = colorResource(id = R.color.gry),
                        modifier = Modifier
                            .size(16.dp)
                            .clickable {
                                onTextChanged("")
                            }
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = colorResource(id = R.color.black),
                cursorColor = if (isFocusedState.value) Color.Black else Color.Transparent,
                unfocusedLeadingIconColor = colorResource(id = colorBase),
                focusedLeadingIconColor = colorResource(id = colorBase),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),

            keyboardActions = KeyboardActions(
                onSearch = {

                    focusManager.clearFocus()
                    keyboardController?.hide()

                }
            ),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            textStyle = TextStyle(
                color = colorResource(id = R.color.black),
                fontWeight = FontWeight.W500,
                fontSize = 16.sp
            ),
        )
    }

    LaunchedEffect(isError) {
        if (isError) {
            focusManager.clearFocus()
        }
    }
    LaunchedEffect(Unit) {
        if (isFocused) {
            focusRequester.requestFocus()
        }
    }
}



