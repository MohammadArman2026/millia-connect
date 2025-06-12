package com.reyaz.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector = icon,
    val hasNews: Boolean = false,
    val badgeCount: Int? = null
)

@Composable
fun CustomBottomNavigationBar(
    items: List<BottomNavItem>,
    selectedRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surface,
    contentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    NavigationBar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount != null) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.error
                                ) {
                                    Text(
                                        text = item.badgeCount.toString(),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            } else if (item.hasNews) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (selectedRoute == item.route) item.selectedIcon else item.icon,
                            contentDescription = item.title
                        )
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = selectedRoute == item.route,
                onClick = { onItemClick(item.route) },
                alwaysShowLabel = true
            )
        }
    }
}

@Composable
fun CustomBottomAppBar(
    modifier: Modifier = Modifier,
    containerColor: androidx.compose.ui.graphics.Color = BottomAppBarDefaults.containerColor,
    contentColor: androidx.compose.ui.graphics.Color = contentColorFor(containerColor),
    tonalElevation: androidx.compose.ui.unit.Dp = BottomAppBarDefaults.ContainerElevation,
    contentPadding: PaddingValues = BottomAppBarDefaults.ContentPadding,
    windowInsets: WindowInsets = BottomAppBarDefaults.windowInsets,
    content: @Composable RowScope.() -> Unit
) {
    BottomAppBar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        contentPadding = contentPadding,
        windowInsets = windowInsets,
        content = content
    )
}

// Sample navigation items for preview
private val sampleNavItems = listOf(
    BottomNavItem(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    ),
    BottomNavItem(
        route = "courses",
        title = "Courses",
        icon = Icons.Default.School,
        hasNews = true
    ),
    BottomNavItem(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person,
        badgeCount = 3
    ),
    BottomNavItem(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
)

@Preview(showBackground = true)
@Composable
private fun CustomBottomNavigationBarPreview() {
    MaterialTheme {
        CustomBottomNavigationBar(
            items = sampleNavItems,
            selectedRoute = "home",
            onItemClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomBottomAppBarPreview() {
    MaterialTheme {
        CustomBottomAppBar {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
    }
}