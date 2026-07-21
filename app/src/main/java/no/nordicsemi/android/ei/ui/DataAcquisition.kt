/*
 * Copyright (c) 2022, Nordic Semiconductor
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package no.nordicsemi.android.ei.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import no.nordicsemi.android.ei.HorizontalPagerTab
import no.nordicsemi.android.ei.R
import no.nordicsemi.android.ei.model.Message
import no.nordicsemi.android.ei.model.Sample
import no.nordicsemi.android.ei.ui.layouts.InfoLayout
import no.nordicsemi.android.ei.util.asMessage
import no.nordicsemi.android.common.ui.R as uiR

@Composable
fun DataAcquisition(
    pagerState: PagerState,
    listStates: List<LazyListState>,
    samples: List<Flow<PagingData<Sample>>>,
    samplingState: Message.Sample
) {
    val tabs = listOf(HorizontalPagerTab.TRAINING, HorizontalPagerTab.TESTING)
    val coroutineScope = rememberCoroutineScope()

    Column {
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = colorResource(id = uiR.color.appBarColor),
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(
                        selectedTabIndex = pagerState.currentPage,
                        matchContentSize = false,
                    ),
                    color = MaterialTheme.colorScheme.onPrimary,
                    width = Dp.Unspecified,
                )
            }
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    text = { Text(text = stringResource(id = tab.title)) },
                    icon = {
                        Icon(
                            painter = rememberVectorPainter(tab.icon),
                            contentDescription = null
                        )
                    },
                    selected = pagerState.currentPage == index,
                    selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                    onClick = {
                        coroutineScope.launch {
                            pagerState.scrollToPage(index)
                        }
                    },
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
        ) { page ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CollectedDataList(
                    state = listStates[page],
                    samplingState = samplingState,
                    pagingDataFlow = samples[page],
                    tab = HorizontalPagerTab.indexed(page)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CollectedDataList(
    state: LazyListState,
    samplingState: Message.Sample,
    pagingDataFlow: Flow<PagingData<Sample>>,
    tab: HorizontalPagerTab
) {
    val samples: LazyPagingItems<Sample> = pagingDataFlow.collectAsLazyPagingItems()
    // Refresh the samples when after a successful sampling
    LaunchedEffect(samplingState) {
        if (samplingState is Message.Sample.Finished && samplingState.error == null) {
            samples.refresh()
        }
    }

    samples.let { lazyPagingItems ->
        OutlinedCard(
            modifier = Modifier.padding(all = 16.dp)
        ) {
            LazyColumn(
                state = state,
            ) {
                if (lazyPagingItems.itemCount > 0) {
                    stickyHeader {
                        Row(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                        ) {
                            Text(
                                text = stringResource(id = R.string.label_col_label),
                                modifier = Modifier.weight(0.4f),
                                style = MaterialTheme.typography.labelLarge,
                            )
                            Text(
                                text = stringResource(id = R.string.label_col_sample_name),
                                modifier = Modifier.weight(0.6f),
                                style = MaterialTheme.typography.labelLarge,
                            )
                            Text(
                                text = stringResource(id = R.string.label_col_length),
                                modifier = Modifier.width(60.dp),
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }
                        HorizontalDivider()
                    }
                }

                items(lazyPagingItems.itemSnapshotList) { sample ->
                    sample?.let { sample ->
                        CollectedDataRow(sample)
                    }
                }

                lazyPagingItems.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item { LoadingIndicator(modifier = Modifier.fillParentMaxSize()) }
                        }

                        loadState.append is LoadState.Loading -> {
                            item { LoadingIndicator() }
                        }

                        loadState.refresh is LoadState.Error -> {
                            val e = lazyPagingItems.loadState.refresh as LoadState.Error
                            item {
                                Error(
                                    message = e.error.asMessage(),
                                    modifier = Modifier.fillParentMaxSize(),
                                    onClickRetry = { retry() }
                                )
                            }
                        }

                        loadState.append is LoadState.Error -> {
                            val e = lazyPagingItems.loadState.append as LoadState.Error
                            item {
                                ErrorItem(
                                    message = e.error.asMessage(),
                                    onClickRetry = { retry() }
                                )
                            }
                        }

                        loadState.refresh is LoadState.NotLoading -> {
                            if (lazyPagingItems.itemCount == 0) item {
                                InfoLayout(
                                    iconPainter = rememberVectorPainter(tab.icon),
                                    text = stringResource(R.string.label_no_collected_data_yet),
                                    modifier = Modifier.fillParentMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CollectedDataRow(
    sample: Sample,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = sample.label,
            modifier = Modifier.weight(0.4f),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = sample.filename,
            modifier = Modifier.weight(0.6f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = "${sample.totalLengthMs.toInt() / 1000}s",
            modifier = Modifier.width(60.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
        )
    }
}

@Composable
private fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Error(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    InfoLayout(
        imageVector = Icons.Outlined.ErrorOutline,
        modifier = modifier,
    ) {
        Text(text = message, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onClickRetry) {
            Text(text = stringResource(R.string.action_try_again))
        }
    }
}

@Composable
private fun ErrorItem(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    CompositionLocalProvider(
        value = LocalContentColor provides MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Icon(
                painter = rememberVectorPainter(image = Icons.Filled.Error),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1.0f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Button(onClick = onClickRetry) {
                Text(text = stringResource(R.string.action_try_again))
            }
        }
    }
}