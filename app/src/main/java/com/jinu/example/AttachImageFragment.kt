package com.jinu.example

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView

import com.image.attacher.utils.ImagePicker
import com.image.attacher.utils.ImageUtils


/***
 * Created by Jinu on 4/22/2016.
 */
class AttachImageFragment : Fragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private var button: Button? = null
    private var imageView: ImageView? = null
    private var use_camera: CheckBox? = null
    private var use_storage_api: CheckBox? = null
    private var save_public_dir: CheckBox? = null

    private var useGallery = true
    private var useStorageApi = false
    private var savePublicDir = false

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_attach_image, container, false)
    }


    override fun onViewCreated(view: View?,
                               savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button = view!!.findViewById(R.id.button)
        imageView = view.findViewById(R.id.imageView2)
        use_camera = view.findViewById(R.id.use_camera)
        use_storage_api = view.findViewById(R.id.use_storage_api)
        save_public_dir = view.findViewById(R.id.save_public_dir)
        button!!.setOnClickListener(this)
        use_camera!!.setOnCheckedChangeListener(this)
        use_storage_api!!.setOnCheckedChangeListener(this)
        save_public_dir!!.setOnCheckedChangeListener(this)
    }


    override fun onClick(v: View) {
        startActivityForResult(ImagePicker.getInstance()
                .setFolderName("JinuImageAttachLib")
                .setCompressWidthHeight(500, 500)
                .useGallery(useGallery)
                .usePictureDir(savePublicDir)
                .useStorageApi(useStorageApi)
                .setIcon(R.drawable.ic_send_white_24dp)
                .build(activity), 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val fileLocation = data!!.data!!.path
            imageView!!.setImageBitmap(ImageUtils.decodeBitmap(fileLocation, 100, 100))
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (use_storage_api === buttonView) {
            useStorageApi = isChecked
        }
        if (use_camera === buttonView) {
            useGallery = !useGallery
        }
        if (save_public_dir === buttonView) {
            savePublicDir = isChecked
        }
    }
}
