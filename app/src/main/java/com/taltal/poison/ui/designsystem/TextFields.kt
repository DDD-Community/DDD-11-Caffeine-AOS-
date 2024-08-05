package com.taltal.poison.ui.designsystem

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.taltal.poison.ui.theme.body_16rg
import com.taltal.poison.ui.theme.dialogue_14rg
import com.taltal.poison.ui.theme.taltal_neutral_10
import com.taltal.poison.ui.theme.taltal_neutral_50
import com.taltal.poison.ui.theme.taltal_neutral_60
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundedTextField(
    textHint: String = "",
    onValueChanged: (String) -> Unit = {},
    checkError: (String) -> Boolean = { false },
    needClearButton: Boolean = false,
    maxLine: Int = 1,
    modifier: Modifier,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var textInput by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    Column(modifier = modifier) {
        TextField(
            value = textInput,
            onValueChange = {
                onValueChanged(it)
                textInput = it
                isError = checkError(it)
            },
            placeholder = {
                Text(
                    text = textHint,
                    style = body_16rg,
                    color = taltal_neutral_50
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    if (isError) Color.Red else taltal_neutral_10,
                    RoundedCornerShape(8.dp)
                ),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = Color.White
            ),
            trailingIcon = {
                if (needClearButton && textInput.isNotEmpty()) {
                    IconButton(onClick = {
                        onValueChanged("")
                        textInput = ""
                        isError = false
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear input",
                            tint = taltal_neutral_60
                        )
                    }
                }
            },
            maxLines = maxLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType,imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    scope.launch {
                        keyboardController?.hide()
                    }
                }
            )
        )
    }
}

@Composable
fun CharacterMessage(text: String) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        text.forEachIndexed { _, char ->
            delay(100) // 각 문자 사이의 지연 시간 (밀리초)
            displayedText += char
        }
    }

    Box(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(0.7f)
            .wrapContentHeight()
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val cornerRadius = 16.dp.toPx()
            val tailWidth = 10.dp.toPx()
            val tailHeight = 10.dp.toPx()

            // 말풍선 본체
            val roundRect = RoundRect(
                left = 0f,
                top = 0f,
                right = size.width - tailWidth,
                bottom = size.height,
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )
            val rectPath = Path().apply {
                addRoundRect(roundRect)
            }

            // 말풍선 꼬리
            val tailPath = Path().apply {
                moveTo(size.width - tailWidth, size.height / 2 - tailHeight / 2)
                lineTo(size.width, size.height / 2)
                lineTo(size.width - tailWidth, size.height / 2 + tailHeight / 2)
                close()
            }

            drawPath(rectPath, color = Color(0xFF3A3F47), style = Fill)
            drawPath(tailPath, color = Color(0xFF3A3F47), style = Fill)
        }

        Text(
            text = displayedText,
            style = dialogue_14rg,
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterStart)
                .background(Color.Transparent)
        )
    }
}

@Preview
@Composable
private fun CharacterMessageView() {
    CharacterMessage("닉네임을\n입력해주세요.")
}