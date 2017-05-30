package es.iridiobis.temporizador.testtools


import android.os.Build
import android.support.test.InstrumentationRegistry
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiObjectNotFoundException
import android.support.test.uiautomator.UiSelector

import timber.log.Timber

object PermissionHandler {

    /**
     * Grants the given permissions. Use in @BeforeClass to grant the permissions for the whole
     * test suit.
     */
    fun allowPermissions(permissions: Array<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand(
                        "pm grant " + InstrumentationRegistry.getTargetContext().packageName
                                + " " + permission)
            }
        }
    }

    /**
     * Interacts with the
     */
    fun allowPermissionsIfNeeded(device: UiDevice) {
        if (Build.VERSION.SDK_INT >= 23) {
            val allowPermissions = device.findObject(UiSelector().text("ALLOW"))
            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click()
                } catch (e: UiObjectNotFoundException) {
                    Timber.e(e, "There is no permissions dialog to interact with ")
                }

            }
        }
    }

    fun denyPermissionsIfNeeded(device: UiDevice) {
        if (Build.VERSION.SDK_INT >= 23) {
            val allowPermissions = device.findObject(UiSelector().text("DENY"))
            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click()
                } catch (e: UiObjectNotFoundException) {
                    Timber.e(e, "There is no permissions dialog to interact with ")
                }

            }
        }
    }

    /**
     * Revokes all the permissions. Use in @BeforeClass or @AfterClass.
     */
    fun revokePermissions(permissions: Array<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand(
                        "pm revoke " + InstrumentationRegistry.getTargetContext().packageName
                                + " " + permission)
            }
        }
    }

}
