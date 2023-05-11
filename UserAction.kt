package com.test.sandbox


enum class UserAction(val textRes: Int, val iconRes: Int) {
    Action1(
        textRes = R.string.action_1,
        iconRes = R.drawable.ic_action_1
    ),
    Action2(
        textRes = R.string.action_2,
        iconRes = R.drawable.ic_action_2
    ),
    Action3(
        textRes = R.string.action_3,
        iconRes = R.drawable.ic_action_3
    ),
    Action4(
        textRes = R.string.action_4,
        iconRes = R.drawable.ic_action_4
    ),
    Action5(
        textRes = R.string.action_5,
        iconRes = R.drawable.ic_action_5
    ),
}