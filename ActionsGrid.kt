package com.test.sandbox

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ActionsGrid(
    modifier: Modifier,
    actions: List<UserAction>,
    onActionClick: (action: UserAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues(all = ActionsGridDefaults.GridPadding),
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(count = ActionsGridDefaults.ColumnsCount),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(space = ActionsGridDefaults.GridPadding),
        horizontalArrangement = Arrangement.spacedBy(space = ActionsGridDefaults.GridPadding),
    ) {
        items(items = actions) { item ->
            ActionItem(
                modifier = Modifier.aspectRatio(ratio = 1f),
                item = item,
            ) {
                onActionClick(item)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActionItem(
    modifier: Modifier,
    item: UserAction,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(size = 8.dp),
        elevation = 0.dp,
        onClick = onClick,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.secondaryVariant),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(id = item.iconRes),
                contentDescription = stringResource(id = item.textRes),
                tint = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
            Text(
                text = stringResource(id = item.textRes),
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center,
            )
        }
    }
}

object ActionsGridDefaults {
    const val ColumnsCount = 2
    val GridPadding = 16.dp
}
