package com.github.serverfrog.bitburnerplugin.services.common

import com.github.serverfrog.bitburnerplugin.Constants
import kotlinx.serialization.Serializable

interface RpcMessage{
    val jsonrpc: String
    val id: Int
    val method: String
}

@Serializable
data class PushFileMessage(
    override val jsonrpc: String = Constants.JSON_RPC_VER,
    override val id: Int,
    override val method: String = "pushFile",
    val params: Params
): RpcMessage {
    @Serializable
    data class Params(val filename: String, val content: String, val server: String = "home")
}

@Serializable
data class PushFileResult(
    val jsonrpc: String,
    val id: Int,
    val result: String? = null,
    val error: String? = null
)

@Serializable
data class GetFileMessage(
    override val jsonrpc: String = Constants.JSON_RPC_VER,
    override val id: Int,
    override val method: String = "getFile",
    val params: Params
) : RpcMessage  {
    @Serializable
    data class Params(val filename: String, val server: String = "home")
}

@Serializable
data class GetFileResult(
    val jsonrpc: String,
    val id: Int,
    val result: String? = null,
    val error: String? = null
)

@Serializable
data class DeleteFileMessage(
    override val jsonrpc: String = Constants.JSON_RPC_VER,
    override val id: Int,
    override val method: String = "deleteFile",
    val params: Params
) : RpcMessage  {
    @Serializable
    data class Params(val filename: String, val server: String = "home")
}

@Serializable
data class DeleteFileResult(
    val jsonrpc: String,
    val id: Int,
    val result: String? = null,
    val error: String? = null
)

@Serializable
data class GetFileNamesMessage(
    override val jsonrpc: String = Constants.JSON_RPC_VER,
    override val id: Int,
    override val method: String = "getFileNames",
    val params: Params = Params()
): RpcMessage {
    @Serializable
    data class Params(val server: String = "home")
}

@Serializable
data class GetFileNamesResult(
    val jsonrpc: String,
    val id: Int,
    val result: Array<String>? = null,
    val error: String? = null
)

@Serializable
data class GetAllFilesMessage(
    override val jsonrpc: String = Constants.JSON_RPC_VER,
    override val id: Int,
    override val method: String = "getAllFiles",
    val params: Params = Params()
): RpcMessage {
    @Serializable
    data class Params(val server: String = "home")
}

@Serializable
data class GetAllFilesResult(
    val jsonrpc: String,
    val id: Int,
    val result: Array<Result>? = null,
    val error: String? = null
) {
    @Serializable
    data class Result(val filename: String, val content: String)
}

@Serializable
data class CalculateRamMessage(
    override val jsonrpc: String = Constants.JSON_RPC_VER,
    override val id: Int,
    override val method: String = "calculateRam",
    val params: Params
) : RpcMessage  {
    @Serializable
    data class Params(val filename: String, val server: String = "home")
}

@Serializable
data class CalculateRamResult(
    val jsonrpc: String,
    val id: Int,
    val result: Int? = null,
    val error: String? = null
)

@Serializable
data class GetDefinitionFileMessage(
    override val jsonrpc: String = Constants.JSON_RPC_VER,
    override val id: Int,
    override val method: String = "getDefinitionFile",

) : RpcMessage

@Serializable
data class GetDefinitionFileResult(
    val jsonrpc: String,
    val id: Int,
    val result: String? = null,
    val error: String? = null
)
