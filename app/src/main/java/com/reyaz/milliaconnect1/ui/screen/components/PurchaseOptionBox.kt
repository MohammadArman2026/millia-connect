/*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.nabssam.bestbook.data.remote.dto.ProductType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseOptionBox(
    modifier: Modifier = Modifier,
    paperbackPrice: Int,
    paperbackDiscount: Int?,
    ebookPrice: Int?,
    ebookDiscount: Int? = null,
//    onTabSelect: (ButtonType) -> Unit ={},
    onTypeSelect: (ProductType) -> Unit,
    productType: ProductType?,
    originalPrice: Int,
//    onEvent: (EventBookDetail) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(if (productType == ProductType.Book) 0 else if (productType == ProductType.ebook) 1 else -1) }
    val tooltipState = rememberTooltipState(isPersistent = true)
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        // MRP Text
        if (originalPrice > paperbackPrice)
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = "MRP ₹${originalPrice}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.LineThrough,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )

        // Custom Tab Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Paperback Tab
            if (paperbackDiscount != null && paperbackDiscount > 0)
                Box(modifier = Modifier.weight(1f)) {
                    CustomPurchaseTab(
                        title = "Paperback",
                        discount = paperbackDiscount,
                        price = paperbackPrice,
                        isSelected = selectedTabIndex == 0,
                        onClick = {
                            selectedTabIndex = 0
//                        onTabSelect(ButtonType.ADD_TO_CART)
                            onTypeSelect(ProductType.Book)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            // Ebook/Quiz Tab
            ebookDiscount?.let {
                Box(modifier = Modifier.weight(1f)) {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Quiz & Ebook Benefits",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text("• Interactive practice questions")
                                    Text("• Instant performance feedback")
                                    Text("• Study anywhere, anytime")
                                    Text("• Regular content updates")
                                    Text("• Previous year papers")
                                }
                            }
                        },
                        state = tooltipState
                    ) {
                        CustomPurchaseTab(
                            title = "Quiz/Ebook",
                            discount = ebookDiscount,
                            price = ebookPrice ?: paperbackPrice,
                            isSelected = selectedTabIndex == 1,
                            onClick = {
                                selectedTabIndex = 1
                                scope.launch {
                                    tooltipState.show()
                                }
//                                onTabSelect(ButtonType.EBOOK)
                                onTypeSelect(ProductType.ebook)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun CustomPurchaseTab(
    title: String,
    discount: Int,
    price: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            )
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontStyle = FontStyle.Italic,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            )
            if (discount > 0) {
                Text(
                    text = "${discount.toInt()}% OFF",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            Text(
                text = "₹${price}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

// Usage in your LazyColumn
*/
