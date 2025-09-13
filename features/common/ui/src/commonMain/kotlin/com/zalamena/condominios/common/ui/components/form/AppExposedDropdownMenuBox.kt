package com.zalamena.condominios.common.ui.components.form

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AppExposedDropdownMenuBox(
    items: List<T>,
    selectedItem: T?,
    onSelect: (T) -> Unit,
    itemText: (T) -> String,
    hint: String = "Select an option"
) {
    var dropdownState by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = dropdownState,
        onExpandedChange = {
            dropdownState = !dropdownState
        },
    ) {
        TextField(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
            readOnly = true,
            value = selectedItem?.let { itemText(it) }?:"",
            label = { Text(hint) },
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownState)
            }
        )
        ExposedDropdownMenu(
            expanded = dropdownState,
            onDismissRequest = { dropdownState = false }
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = {
                        Text(itemText(it))
                    },
                    onClick = {
                        onSelect(it)
                        dropdownState = false
                    }
                )
            }
        }
    }
}