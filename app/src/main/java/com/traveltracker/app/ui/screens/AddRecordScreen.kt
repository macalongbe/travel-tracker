package com.traveltracker.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.traveltracker.app.data.local.TravelRecord
import com.traveltracker.app.ui.viewmodel.TravelViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordScreen(
    viewModel: TravelViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    var country by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var entryDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var exitDate by remember { mutableStateOf(System.currentTimeMillis()) }
    
    var showEntryDatePicker by remember { mutableStateOf(false) }
    var showExitDatePicker by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Travel Record") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                label = { Text("Country") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = region,
                onValueChange = { region = it },
                label = { Text("Region") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Entry Date
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showEntryDatePicker = true }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Entry Date")
                Text(
                    text = dateFormat.format(Date(entryDate)),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Exit Date
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showExitDatePicker = true }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Exit Date")
                Text(
                    text = dateFormat.format(Date(exitDate)),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes (Optional)") },
                singleLine = false,
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            if (showError) {
                Text(
                    text = "Please fill in Country and Region",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = {
                    if (country.isBlank() || region.isBlank()) {
                        showError = true
                        return@Button
                    }
                    val record = TravelRecord(
                        country = country,
                        region = region,
                        entryDate = entryDate,
                        exitDate = exitDate,
                        notes = notes.ifBlank { null }
                    )
                    viewModel.addRecord(record)
                    onNavigateBack()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save Record")
            }
        }

        if (showEntryDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = entryDate
            )
            DatePickerDialog(
                onDismissRequest = { showEntryDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { entryDate = it }
                        showEntryDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEntryDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showExitDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = exitDate
            )
            DatePickerDialog(
                onDismissRequest = { showExitDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { exitDate = it }
                        showExitDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}