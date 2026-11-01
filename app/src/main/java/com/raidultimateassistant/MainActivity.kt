package com.raidultimateassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    item {
                        Text(
                            "RAID Ultimate Assistant",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(Modifier.height(20.dp))
                        listOf(
                            "Şampiyonlar",
                            "Ekipmanlar",
                            "Hydra Takımı",
                            "Zindan Farmı",
                            "Arena",
                            "Ekran Görüntüsü Analizi"
                        ).forEach { section ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            ) {
                                Text(
                                    section,
                                    modifier = Modifier.padding(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
