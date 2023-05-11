package com.test.sandbox

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.NonDisposableHandle.parent
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalComposeUiApi::class)
@Destination
@Composable
fun UserDetails(
    navigator: DestinationsNavigator,
    userKey: String? = null,
    user: UserProfile? = null,
    viewModel: UserDetailsViewModel = getViewModel {
        parametersOf(userKey, user)
    },
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            IconTopBar(
                modifier = Modifier.fillMaxWidth(),
                onNavigateUp = { navigator.navigateUp() },
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val (bigImage, smallImage) = createRefs()
                    Image(
                        modifier = Modifier
                            .size(size = 85.dp)
                            .clip(shape = CircleShape)
                            .constrainAs(bigImage) {
                                linkTo(
                                    start = parent.start,
                                    end = parent.end
                                )
                            },
                        painter = painterResource(id = R.drawable.user_image),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth
                    )
                    if (viewModel.viewState.user != null) {
                        Image(
                            modifier = Modifier
                                .constrainAs(smallImage) {
                                    linkTo(
                                        start = bigImage.start,
                                        end = bigImage.end,
                                        startMargin = 75.dp
                                    )
                                }
                                .size(size = 44.dp)
                                .background(
                                    color = MaterialTheme.colors.surface,
                                    shape = CircleShape
                                )
                                .padding(6.dp),
                            painter = painterResource(id = R.drawable.ic_user),
                            contentDescription = null,
                            contentScale = ContentScale.Inside

                        )
                    }

                }
            }
        }
    ) { paddingValues ->
        val context = LocalContext.current
        LoadContent(
            navigator = navigator,
            paddingValues = paddingValues,
            viewState = viewModel.viewState,
            context = context,
            onSaveArgs = { user ->
                viewModel.saveUserInArgs(user)
            }
        )
    }

    ErrorDialog(
        visible = viewModel.viewState.error != null,
        text = viewModel.viewState.error?.localizedMessage.orEmpty(),
        onDismiss = viewModel::onErrorShown,
    )
}


@Composable
private fun LoadContent(
    navigator: DestinationsNavigator,
    paddingValues: PaddingValues,
    viewState: UserDetailsViewState,
    onSaveArgs: (user: UserProfile) -> Unit,
    context: Context,
) {

    when {
        viewState.showProgress ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
            }
        viewState.user != null -> {
            val user = viewState.user
            Content(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues),
                user = user,
                onGeneralInfo = {
                    navigator.navigate(UserGeneralInfoDestination(user = user))
                },
                onActionClick = { action ->
                    onSaveArgs.invoke(user)
                    when (action) {
                        UserAction.Action1 ->
                            navigator.navigate(Action1Destination(userKey = user.id))
                        UserAction.Action2 ->
                            navigator.navigate(Action2Destination(userKey = user.id))
                        UserAction.Action3 ->
                            navigator.navigate(Action3Destination(userKey = user.id))
                        UserAction.Action4 ->
                            navigator.navigate(Action4Destination(userKey = user.id))
                        UserAction.Action5 ->
                            navigator.navigate(Action5Destination(userKey = user.id))
                    }
                }
            )
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    user: UserProfile,
    onGeneralInfo: () -> Unit,
    onActionClick: (action: UserAction) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 2.dp),
                text = user.name,
                style = MaterialTheme.typography.h3,
            )
            Text(
                text = stringResource(id = R.string.user_id_format, user.bin),
                style = MaterialTheme.typography.body1.copy(color = TextSecondary),
                textAlign = TextAlign.Center,
            )
        }
        GeneralInfoItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            role = user.location,
            onClick = onGeneralInfo,
        )
        TimeAndLocationInfoItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            user = user,
        )
        ActionsGrid(
            modifier = Modifier.fillMaxWidth(),
            actions = user.actions(),
            onActionClick = onActionClick,
            contentPadding = PaddingValues(
                top = 0.dp,
                start = ActionsGridDefaults.GridPadding,
                end = ActionsGridDefaults.GridPadding,
                bottom = ActionsGridDefaults.GridPadding + 56.dp,
            ),
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun GeneralInfoItem(
    modifier: Modifier,
    role: UserRole,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier,
        border = BorderStroke(
            color = MaterialTheme.colors.primary,
            width = 1.dp,
        ),
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colors.surface,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_round_event_note),
                contentDescription = null,
            )
            Column(
                modifier = Modifier
                    .weight(weight = 1f)
                    .padding(horizontal = 12.dp, vertical = 13.dp)
            ) {
                CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.caption) {
                    Text(text = stringResource(id = R.string.general_information))
                }
                Spacer(modifier.height(6.dp))
                Text(
                    text = stringResource(id = R.string.role_format, role.capitalizedName()),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.body2
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_round_arrow_forward),
                contentDescription = stringResource(id = R.string.general_information),
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
private fun TimeAndLocationInfoItem(
    modifier: Modifier,
    user: UserProfile,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.background,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier
                .weight(weight = 1.1f)
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(13.dp),
                        painter = painterResource(id = R.drawable.ic_time),
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource(id = R.string.was_registered),
                        style = MaterialTheme.typography.caption
                    )
                }
                Spacer(modifier.height(6.dp))
                Text(
                    text = if (user.timeAfterRegistration.toInt() > 1 )
                        stringResource(id = R.string.days_ago_format, user.timeAfterRegistration)
                    else
                        stringResource(id = R.string.day_ago_format, user.timeAfterRegistration),
                    style = MaterialTheme.typography.body1
                )
            }
            Column(
                modifier = Modifier
                .weight(weight = 1f)
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(13.dp),
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource(id = R.string.location),
                        style = MaterialTheme.typography.caption
                    )
                }
                Spacer(modifier.height(6.dp))
                Text(
                    text = user.currentLocation.name,
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserDetailsContentPreview() {
    AppTheme {
        Content(
            modifier = Modifier.fillMaxSize(),
            user = DummyUser,
            onGeneralInfo = {},
            onActionClick = {},
        )
    }
}