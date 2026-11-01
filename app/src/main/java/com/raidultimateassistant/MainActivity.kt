
package com.raidultimateassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
                var editingIndex by remember { mutableStateOf<Int?>(null) }
                var name by remember { mutableStateOf("") }
                var level by remember { mutableStateOf("60") }
                var stars by remember { mutableStateOf("6") }
                var deleteIndex by remember { mutableStateOf<Int?>(null) }

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
                        onClick = {
                            editingIndex = null
                            name = ""
                            level = "60"
                            stars = "6"
                            showDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("+ Şampiyon Ekle")
                    }

                    Spacer(Modifier.height(12.dp))

                    LazyColumn {
                        itemsIndexed(champions) { index, champion ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Text(
                                        champion.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text("Seviye: ${champion.level}")
                                    Text("Yıldız: ${champion.stars}")

                                    Row {
                                        TextButton(
                                            onClick = {
                                                editingIndex = index
                                                name = champion.name
                                                level = champion.level.toString()
                                                stars = champion.stars.toString()
                                                showDialog = true
                                            }
                                        ) {
                                            Text("Düzenle")
                                        }

                                        TextButton(
                                            onClick = {
                                                deleteIndex = index
                                            }
                                        ) {
                                            Text("Sil")
                                        }
                                    }
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
                            Text(
                                if (editingIndex == null)
                                    "Yeni Şampiyon"
                                else
                                    "Şampiyonu Düzenle"
                            )
                        },
                        text = {
                            Column {
                                OutlinedTextField(
                                    value = name,
                                    onValueChange = { name = it },
                                    label = { Text("Şampiyon adı") }
                                )

                                OutlinedTextField(
                                    value = level,
                                    onValueChange = { level = it },
                                    label = { Text("Seviye (1-60)") }
                                )

                                OutlinedTextField(
                                    value = stars,
                                    onValueChange = { stars = it },
                                    label = { Text("Yıldız (1-6)") }
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
                                        val champion = Champion(
                                            name.trim(),
                                            l,
                                            s
                                        )

                                        val index = editingIndex

                                        if (index == null) {
                                            champions.add(champion)
                                        } else {
                                            champions[index] = champion
                                        }

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

                if (deleteIndex != null) {
                    AlertDialog(
                        onDismissRequest = {
                            deleteIndex = null
                        },
                        title = {
                            Text("Şampiyonu Sil")
                        },
                        text = {
                            Text("Bu şampiyonu silmek istediğine emin misin?")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    deleteIndex?.let {
                                        champions.removeAt(it)
                                    }
                                    deleteIndex = null
                                }
                            ) {
                                Text("Sil")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    deleteIndex = null
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
