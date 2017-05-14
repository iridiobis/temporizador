package es.iridiobis.temporizador.core.realm

import io.realm.DynamicRealm
import io.realm.RealmMigration


class Migration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        var version = oldVersion
        /*
         *  Migrate starting on version 0
         *   Add the fields background, image and thumbnail to RealmTask
         *   Use the id to get the values of those fields, as it was done in the first version
         *   of the image storage.
         */
        if (version == 0L) {
            realm.schema.get("RealmTask")
                    .addField("background", String::class.java)
                    .addField("image", String::class.java)
                    .addField("thumbnail", String::class.java)
                    .transform {
                        it ->
                        val id: Long = it.get("id")
                        val idName = id.toString()
                        it.set("background", idName)
                        it.set("image", idName + "_small")
                        it.set("thumbnail", idName + "_thumbnail")
                    }
            version++
        }
        //Migrate starting on 1
        //...
    }
}