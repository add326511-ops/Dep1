package com.raidultimateassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class Feature(val title: String, val description: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RaidAssistantApp() }
    }
}

@Composable
private fun RaidAssistantApp() {
    val features = listOf(
        Feature("Şampiyonlar", "Şampiyon kadronu ve gelişim durumunu yönet"),
        Feature("Ekipmanlar", "Artefakt ve aksesuar envanterini düzenle"),
        Feature("Hydra Takımı", "Hydra için takım planlama alanı"),
        Feature("Zindan Farmı", "Dragon ve diğer zindanlar için farm planı"),
        Feature("Arena", "Arena takım notları ve önerileri"),
        Feature("Ekran Görüntüsü Analizi", "Oyun ekran görüntülerini içe aktarma alanı")
    )
    var selected by remember { mutableStateOf<String?>(null) }

    MaterialTheme(colorScheme = darkColorScheme()) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("RAID Ultimate Assistant", fontWeight = FontWeight.Bold) }
                )
            }
        ) { padding ->
            if (selected == null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text("Hoş geldin!", style = MaterialTheme.typography.headlineMedium)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "RAID: Shadow Legends için Türkçe kadro ve strateji yardımcın.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    items(features) { feature ->
                        Card(
                            onClick = { selected = feature.title },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(18.dp)) {
                                Text(feature.title, style = MaterialTheme.typography.titleLarge)
                                Spacer(Modifier.height(4.dp))
                                Text(feature.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                    item {
                        Text(
                            "Not: Bu uygulama yardımcı analiz içindir; oyun hesabına veya oyun dosyalarına müdahale etmez.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(selected!!, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        "Bu bölüm başlangıç sürümünde hazırlanıyor. Sonraki adımda bu ekrana gerçek kayıt ve analiz özellikleri eklenecek.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Button(onClick = { selected = null }) { Text("Ana menüye dön") }
                }
            }
        }
    }
}
