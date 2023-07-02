package com.dexter.newsboard.ui.headlines

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.dexter.newsboard.R
import com.dexter.newsboard.data.remote.datasource.model.Article
import com.dexter.newsboard.ui.ui.theme.NewsBoardTheme

@OptIn(ExperimentalMaterial3Api::class)
class HomeActivity : ComponentActivity() {

    private val headlinesViewModel by viewModels<HeadlinesViewModel>()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsBoardTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("News") },
                            navigationIcon = {
                                IconButton(
                                    onClick = { headlinesViewModel.refreshHeadlines() }
                                ) {
                                    Icon(
                                        Icons.Sharp.Refresh,
                                        contentDescription = "Refresh"
                                    )
                                }
                            }
                        )
                    }
                ) {
                    HeadlineList(
                        headlinesViewModel,
                        it.calculateBottomPadding(),
                        it.calculateTopPadding(),
                        it.calculateStartPadding(LayoutDirection.Ltr),
                        it.calculateEndPadding(LayoutDirection.Ltr)
                    )
                }
                // A surface container using the 'background' color from the theme
                /*  Surface(
                      modifier = Modifier.fillMaxSize(),
                      color = MaterialTheme.colorScheme.background
                  ) {
                      HeadlineList(headlinesViewModel)
                  }*/
            }
        }
        headlinesViewModel.getHeadlines()
    }
}

@Composable
fun HeadlineList(
    headlinesViewModel: HeadlinesViewModel,
    calculateBottomPadding: Dp,
    calculateTopPadding: Dp,
    calculateStartPadding: Dp,
    calculateEndPadding: Dp
) {
    val headlineUIState by headlinesViewModel.headlinesState.collectAsStateWithLifecycle(
        initialValue = HeadlinesUIState()
    )

    HeadLineLazyList(
        headlineUIState,
        calculateBottomPadding,
        calculateTopPadding,
        calculateStartPadding,
        calculateEndPadding
    )
}

@Composable
fun HeadLineLazyList(
    headlineUIState: HeadlinesUIState,
    calculateBottomPadding: Dp,
    calculateTopPadding: Dp,
    calculateStartPadding: Dp,
    calculateEndPadding: Dp
) {
    Box(
        Modifier
            .padding(
                start = calculateStartPadding,
                end = calculateEndPadding,
                top = calculateTopPadding,
                bottom = calculateBottomPadding
            )
            .fillMaxSize()
    ) {
        if (headlineUIState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.TopCenter)
            )
        }
        if (!headlineUIState.isLoading) {
            headlineUIState.articles.run {
                LazyColumn(
                    modifier = Modifier.align(Alignment.TopCenter),
                ) {
                    items(headlineUIState.articles) { article ->
                        HeadlineCard(article = article)
                    }
                }
            }
        }
    }
}

@Composable
fun HeadlineCard(article: Article) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clickable { }
            .background(color = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(5.dp)
    ) {
        Box(Modifier.wrapContentSize()) {
            ScalingHeadlineImage(
                model = article.urlToImage,
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Column(Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .wrapContentHeight()
                        .fillMaxWidth(0.5F)
                        .align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    article.source?.let { source ->
                        Box(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth(0.5F)
                                .padding(2.dp),
                        ) {
                            Text(
                                text = source,
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.End
                                ),
                                modifier = Modifier
                                    .wrapContentSize()
                                    .background(
                                        color = Color.Red,
                                        shape = RoundedCornerShape(2.dp)
                                    )
                                    .align(Alignment.CenterEnd),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Text(
                        text = article.timePassed,
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .wrapContentHeight()
                            .fillMaxWidth(0.5F)
                            .padding(end = 2.dp),
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Start
                        )
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(5.dp)
                        .wrapContentSize(),
                    text = article.title,
                    style = TextStyle(color = Color.White, fontSize = 18.sp),
                    overflow = TextOverflow.Ellipsis
                )

            }

        }
    }
}


@Composable
fun ScalingHeadlineImage(
    model: String?,
    contentDescription: String,
    contentScale: ContentScale,
    modifier: Modifier
) {
    val duration = 4000
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1F,
        targetValue = 1.2F,
        animationSpec = infiniteRepeatable(
            animation =
            tween(duration), repeatMode = RepeatMode.Reverse
        ),
    )

    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale

        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewsBoardTheme {
        HeadLineLazyList(
            HeadlinesUIState(
                isLoading = true,
                articles = listOf(
                    Article(
                        234234,
                        "ok",
                        "Rajiv Shukla",
                        "Congress leader Rahul Gandhi has moved the Gujarat High Court seeking a stay on his conviction in the defamation case over his remark",
                        "",
                        "https://www.google.com",
                        "https://picsum.photos/id/237/200/300",
                        "13 minutes",
                        ""
                    )
                )
            ),
            10.dp,
            10.dp,
            10.dp,
            10.dp
        )
    }
}