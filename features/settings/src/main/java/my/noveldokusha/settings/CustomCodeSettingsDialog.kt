package my.noveldokusha.settings

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

private const val CUSTOM_CODE_PREFS = "noveld_custom_web_code"

@Composable
internal fun CustomCodeSettingsDialog(
    onDismiss: () -> Unit,
    onSaved: () -> Unit = {},
) {
    val context = LocalContext.current
    val prefs = remember(context) {
        context.getSharedPreferences(CUSTOM_CODE_PREFS, Context.MODE_PRIVATE)
    }
    var selectedTab by remember { mutableIntStateOf(0) }
    var javascript by remember { mutableStateOf(prefs.getString("javascript", "").orEmpty()) }
    var css by remember { mutableStateOf(prefs.getString("css", "").orEmpty()) }
    var javascriptEnabled by remember { mutableStateOf(prefs.getBoolean("javascript_enabled", true)) }
    var cssEnabled by remember { mutableStateOf(prefs.getBoolean("css_enabled", true)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("JavaScript e CSS") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("JavaScript") },
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("CSS") },
                    )
                }
                if (selectedTab == 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Ativar JavaScript")
                        Switch(checked = javascriptEnabled, onCheckedChange = { javascriptEnabled = it })
                    }
                    OutlinedTextField(
                        value = javascript,
                        onValueChange = { javascript = it },
                        modifier = Modifier.fillMaxWidth().height(260.dp),
                        placeholder = { Text("// Código JavaScript para os capítulos") },
                        textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Ativar CSS")
                        Switch(checked = cssEnabled, onCheckedChange = { cssEnabled = it })
                    }
                    OutlinedTextField(
                        value = css,
                        onValueChange = { css = it },
                        modifier = Modifier.fillMaxWidth().height(260.dp),
                        placeholder = { Text("/* CSS para personalizar os capítulos */") },
                        textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                    )
                }
                Text(
                    "Os códigos são aplicados apenas em páginas de capítulos abertas pelo leitor Web.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    prefs.edit()
                        .putString("javascript", javascript)
                        .putString("css", css)
                        .putBoolean("javascript_enabled", javascriptEnabled)
                        .putBoolean("css_enabled", cssEnabled)
                        .apply()
                    onSaved()
                    onDismiss()
                }
            ) { Text("Salvar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
    )
}
