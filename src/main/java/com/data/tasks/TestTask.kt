package com.data.tasks

import com.data.utils.DislogLogger

class TestTask: Thread() {

    val logger = DislogLogger(TestTask::class.java)
    override fun run() {
        logger.info("Test")
    }
}