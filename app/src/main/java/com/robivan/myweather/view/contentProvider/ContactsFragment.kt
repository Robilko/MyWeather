package com.robivan.myweather.view.contentProvider

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.robivan.myweather.R
import com.robivan.myweather.databinding.FragmentContactsBinding
import kotlinx.android.synthetic.main.fragment_contacts.*

const val REQUEST_CODE = 42

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ContactsAdapter
    private var dataContacts: MutableList<List<String?>> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        adapter = ContactsAdapter(dataContacts)
        contacts_fragment_recycler_view.adapter = adapter

    }

    // Проверяем, разрешено ли чтение контактов
    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    //Доступ к контактам на телефоне есть
                    getContacts()
                }
                //Опционально: если нужно пояснение перед запросом разрешений
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle(getString(R.string.access_to_contacts))
                        .setMessage(getString(R.string.explanation_of_access_needed))
                        .setPositiveButton(getString(R.string.dialog_give_access)) { _, _ -> requestPermission() }
                        .setNegativeButton(getString(R.string.dialog_decline_access)) { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                }
                else -> {
                    //Запрашиваем разрешение
                    requestPermission()
                }
            }
        }
    }

    @SuppressLint("Range")
    private fun getContacts() {
        context?.let {
            // Получаем ContentResolver у контекста
            val contentResolver: ContentResolver = it.contentResolver
            // Отправляем запрос на получение контактов и получаем ответ в виде Cursor
            val cursorWithContacts: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )

            cursorWithContacts?.let { cursorContacts ->
                for (i in 0..cursorContacts.count) {
                    // Переходим на позицию в Cursor
                    if (cursorContacts.moveToPosition(i)) {
                        // Берём из Cursor столбец с именем
                        val name =
                            cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        val id =
                            cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID))
                        var phoneNumber: String? = null
                        val cursorWithPhones: Cursor? = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID + " = " + id,
                            null,
                            null
                        )
                        cursorWithPhones?.let { cursorPhones ->
                            for (j in 0..cursorPhones.count) {
                                if (cursorPhones.moveToPosition(j)) {
                                    phoneNumber = cursorPhones.getString(
                                        cursorPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                    )
                                }
                            }
                        }
                        cursorWithPhones?.close()
                        phoneNumber?.let { dataContacts.add(listOf(name, phoneNumber)) }
                    }
                }
            }
            cursorWithContacts?.close()
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    // Обратный вызов после получения разрешений от пользователя
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                // Проверяем, дано ли пользователем разрешение по нашему запросу
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts()
                } else {
                    // Поясните пользователю, что экран останется пустым, потому что доступ к контактам не предоставлен
                    context?.let {
                        AlertDialog.Builder(it)
                            .setTitle(getString(R.string.access_to_contacts))
                            .setMessage(getString(R.string.explanation_of_access_denied))
                            .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                    }
                }
                return
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = ContactsFragment()
    }
}