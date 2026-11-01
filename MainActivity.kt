
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
import org.json.JSONArray
import org.json.JSONObject

data class Champion(
    val name: String,
    val level: Int,
    val stars: Int,
    val element: String,
    val faction: String,
    val rarity: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val prefs = remember {
                    getSharedPreferences("raid_data", MODE_PRIVATE)
                }

                val champions = remember {
                    mutableStateListOf<Champion>().apply {
                        val array = JSONArray(
                            prefs.getString("champions", "[]")
                        )

                        for (i in 0 until array.length()) {
                            val item = array.getJSONObject(i)

                            add(
                                Champion(
                                    item.getString("name"),
                                    item.getInt("level"),
                                    item.getInt("stars"),
                                    item.optString("element", "Büyü"),
                                    item.optString("faction", "Bilinmiyor"),
                                    item.optString("rarity", "Efsanevi")
                                )
                            )
                        }
                    }
                }

                fun saveChampions() {
                    val array = JSONArray()

                    champions.forEach { champion ->
                        val item = JSONObject()
                        item.put("name", champion.name)
                        item.put("level", champion.level)
                        item.put("stars", champion.stars)
                        item.put("element", champion.element)
                        item.put("faction", champion.faction)
                        item.put("rarity", champion.rarity)
                        array.put(item)
                    }

                    prefs.edit()
                        .putString("champions", array.toString())
                        .apply()
                }

                var showDialog by remember { mutableStateOf(false) }
                var editingIndex by remember { mutableStateOf<Int?>(null) }
                var name by remember { mutableStateOf("") }
                var level by remember { mutableStateOf("60") }
                var stars by remember { mutableStateOf("6") }
                var element by remember { mutableStateOf("Büyü") }
                var faction by remember { mutableStateOf("") }
                var rarity by remember { mutableStateOf("Efsanevi") }
                var deleteIndex by remember { mutableStateOf<Int?>(null) }

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
                            element = "Büyü"
                            faction = ""
                            rarity = "Efsanevi"
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
                                    Text("Element: ${champion.element}")
                                    Text("Hizip: ${champion.faction}")
                                    Text("Nadirlik: ${champion.rarity}")

                                    Row {
                                        TextButton(
                                            onClick = {
                                                editingIndex = index
                                                name = champion.name
                                                level = champion.level.toString()
                                                stars = champion.stars.toString()
                                                element = champion.element
                                                faction = champion.faction
                                                rarity = champion.rarity
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
                            Column(
                                modifier = Modifier.heightIn(max = 420.dp)
                            ) {
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

                                OutlinedTextField(
                                    value = element,
                                    onValueChange = { element = it },
                                    label = {
                                        Text("Element: Büyü, Ruh, Kuvvet, Boşluk")
                                    }
                                )

                                OutlinedTextField(
                                    value = faction,
                                    onValueChange = { faction = it },
                                    label = { Text("Hizip") }
                                )

                                OutlinedTextField(
                                    value = rarity,
                                    onValueChange = { rarity = it },
                                    label = {
                                        Text("Nadirlik: Efsanevi, Epik, Nadir...")
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
                                        s != null && s in 1..6 &&
                                        element.isNotBlank() &&
                                        faction.isNotBlank() &&
                                        rarity.isNotBlank()
                                    ) {
                                        val champion = Champion(
                                            name.trim(),
                                            l,
                                            s,
                                            element.trim(),
                                            faction.trim(),
                                            rarity.trim()
                                        )

                                        val index = editingIndex

                                        if (index == null) {
                                            champions.add(champion)
                                        } else {
                                            champions[index] = champion
                                        }

                                        saveChampions()
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
                                    saveChampions()
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
