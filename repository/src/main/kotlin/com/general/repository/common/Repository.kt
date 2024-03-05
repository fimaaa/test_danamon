package com.general.repository.common

import com.general.model.common.UIText
import com.general.model.common.ViewState

interface Repository {
    val suspendOnExceptionError: ViewState.ERROR<Nothing>
        get() = ViewState.ERROR(msg = UIText.DynamicString("Terjadi kesalahan"), code = 502)
}