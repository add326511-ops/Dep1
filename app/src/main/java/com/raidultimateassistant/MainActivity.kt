package com.raidultimateassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Champion(
    val name: String,
    val level: Int,
    val stars: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                var showDialog by remember { mutableStateOf(false) }
                var name by remember { mutableStateOf("") }
                var level by remember { mutableStateOf("60") }
                var stars by remember { mutableStateOf("6") }

                val champions = remember {
                    mutableStateListOf<Champion>()
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        "RAID Ultimate Assistant",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(
                        "Şampiyonlarım",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Button(
                        onClick = { showDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("+ Şampiyon Ekle")
                    }

                    Spacer(Modifier.height(12.dp))

                    LazyColumn {
                        items(champions) { champion ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(champion.name)
                                    Text("Seviye: ${champion.level}")
                                    Text("Yıldız: ${champion.stars}")
                                }
                            }
                        }
                    }
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialog = false
                        },
                        title = {
                            Text("Yeni Şampiyon")
                        },
                        text = {
                            Column {
                                OutlinedTextField(
                                    value = name,
                                    onValueChange = { name = it },
                                    label = {
                                        Text("Şampiyon adı")
                                    }
                                )

                                OutlinedTextField(
                                    value = level,
                                    onValueChange = { level = it },
                                    label = {
                                        Text("Seviye (1-60)")
                                    }
                                )

                                OutlinedTextField(
                                    value = stars,
                                    onValueChange = { stars = it },
                                    label = {
                                        Text("Yıldız (1-6)")
                                    }
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    val l = level.toIntOrNull()
                                    val s = stars.toIntOrNull()

                                    if (
                                        name.isNotBlank() &&
                                        l != null && l in 1..60 &&
                                        s != null && s in 1..6
                                    ) {
                                        champions.add(
                                            Champion(
                                                name.trim(),
                                                l,
                                                s
                                            )
                                        )
                                        name = ""
                                        level = "60"
                                        stars = "6"
                                        showDialog = false
                                    }
                                }
                            ) {
                                Text("Kaydet")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showDialog = false
                                }
                            ) {
                                Text("İptal")
                            }
                        }
                    )
                }
            }
        }
    }
}
