package org.tiffinservice.app


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class FaqItem(
    val category: String,
    val question: String,
    val answer: String
)

fun dummyFaqs(): List<FaqItem> = listOf(
    FaqItem(
        "Getting Started",
        "How do I place my first order?",
        "Browse the menu, add items to cart, and checkout."
    ),
    FaqItem(
        "Getting Started",
        "How does the subscription work?",
        "You can set recurring meal plans and manage them anytime."
    ),
    FaqItem(
        "Ordering & Menu",
        "When is the cut-off time for ordering?",
        "Orders for same-day delivery must be placed before 10 AM."
    ),
    FaqItem(
        "Ordering & Menu",
        "Can I customize my meal?",
        "Some items have customization options. Check the item details."
    ),
    FaqItem(
        "Payments & Billing",
        "What payment methods are accepted?",
        "We accept debit/credit cards and digital wallets."
    ),
    FaqItem(
        "Delivery",
        "What are your delivery areas and times?",
        "We deliver within 10 miles between 12 PM and 2 PM."
    ),
)

@Composable
fun SearchBar(value: String, onValueChange: (String) -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFEDEDED))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)

            Spacer(Modifier.width(8.dp))

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { inner ->
                    if (value.isEmpty()) {
                        Text("Search for a question...", color = Color.Gray)
                    }
                    inner()
                }
            )
        }
    }
}

@Composable
fun FaqSection(title: String, items: List<FaqItem>) {

    if (items.isEmpty()) return

    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 12.dp)
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEach { item ->
            FaqItemCard(item)
        }
    }
}

@Composable
fun FaqItemCard(item: FaqItem) {

    var expanded by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(12.dp)
    ) {

        Row(
            Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = item.question,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier.rotate(if (expanded) 90f else -90f)
            )
        }

        AnimatedVisibility(visible = expanded) {
            Text(
                text = item.answer,
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}


@Composable
fun HelpCard(
    onEmail: () -> Unit,
    onCall: () -> Unit
) {

    val Primary = Color(0xFFF48C25)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Primary.copy(alpha = 0.1f))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Text("Still need help?", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Text(
            "If you can't find the answer you're looking for, please get in touch with our support team.",
            textAlign = TextAlign.Center,
            color = Color.Gray,
            fontSize = 14.sp
        )

        Button(
            onClick = onEmail,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(Primary)
        ) {
            Icon(Icons.Default.Email, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Email Us")
        }

        OutlinedButton(
            onClick = onCall,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
        ) {
            Icon(Icons.Default.Call, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Call Us")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpAndSupportScreen(
    onBack: () -> Unit = {},
    onEmail: () -> Unit = {},
    onCall: () -> Unit = {}
) {

    val BgLight = Color(0xFFF8F7F5)
    val Primary = Color(0xFFF48C25)

    var query by remember { mutableStateOf("") }

    val faqs = dummyFaqs().filter {
        it.question.contains(query, ignoreCase = true)
    }

    Scaffold(
        containerColor = BgLight
    ) { padding ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {

            // Search Bar
            SearchBar(
                value = query,
                onValueChange = { query = it }
            )

            Spacer(Modifier.height(20.dp))

            // Grouped FAQ Sections
            FaqSection(title = "Getting Started", items = faqs.filter { it.category == "Getting Started" })
            FaqSection(title = "Ordering & Menu", items = faqs.filter { it.category == "Ordering & Menu" })
            FaqSection(title = "Payments & Billing", items = faqs.filter { it.category == "Payments & Billing" })
            FaqSection(title = "Delivery", items = faqs.filter { it.category == "Delivery" })

            Spacer(Modifier.height(30.dp))

            // Help Card
            HelpCard(
                onEmail = onEmail,
                onCall = onCall
            )

            Spacer(Modifier.height(30.dp))
        }
    }
}
