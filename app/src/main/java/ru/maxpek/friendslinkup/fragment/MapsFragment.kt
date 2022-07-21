package ru.maxpek.friendslinkup.fragment

import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map

import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.FragmentMapsBinding
import ru.maxpek.friendslinkup.util.PointArg


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MapsFragment : Fragment() {
    private var binding: FragmentMapsBinding? = null

    var mapObjects: MapObject? = null

    private val objectTapListener = GeoObjectTapListener { geo ->
        val selectionMetadata : GeoObjectSelectionMetadata =
            geo.geoObject.metadataContainer.getItem(GeoObjectSelectionMetadata::class.java)

        binding?.map?.map?.selectGeoObject(selectionMetadata.id, selectionMetadata.layerId)

        true
    }

    private val inputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {
            binding?.map?.map?.mapObjects?.clear()
        }

        override fun onMapLongTap(map: Map, point: Point) {
            if (mapObjects != null) {
                binding?.map?.map?.mapObjects?.clear()
            }
            binding?.map?.map?.deselectGeoObject()

            mapObjects = binding?.map?.map?.mapObjects?.addPlacemark(
                point,
                ImageProvider.fromResource(context, R.drawable.search_result)
            )

            Snackbar.make(
                binding?.root!!, R.string.addGeo,
                BaseTransientBottomBar.LENGTH_INDEFINITE
            ).setAction(R.string.add)
                { findNavController().navigate(R.id.action_mapsFragment_to_newPostFragment, ///Передача даннных для сохранения
                    Bundle().apply { pointArg = point })}.show()

        }
    }

    companion object{
        private val TARGET_LOCATION = Point(59.945933, 30.320045)
        var Bundle.pointArg: Point by PointArg

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this.context)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentMapsBinding.inflate(inflater, container, false)
        this.binding = binding
        val mapView = binding.map.apply {
            map.addInputListener(inputListener)
            map.addTapListener(objectTapListener)
        }


        if (arguments?.pointArg != null) {
            mapView.map.move(CameraPosition(arguments?.pointArg!!, 14.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 5F),
                null)
            mapView.map?.mapObjects?.addPlacemark(
                arguments?.pointArg!!,
                ImageProvider.fromResource(context, R.drawable.search_result)
            )

        } else {
            mapView.map.move(
                CameraPosition(TARGET_LOCATION, 14.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 5F),
                null
            )
        }



        binding.plus.setOnClickListener {
            mapView.map.move(CameraPosition(mapView.map.cameraPosition.target,mapView.map.cameraPosition.zoom+1,
                0.0f, 0.0f),
                Animation(Animation.Type.LINEAR, 0.5F),
                null)

        }

        binding.minus.setOnClickListener {
            mapView.map.move(CameraPosition(mapView.map.cameraPosition.target,mapView.map.cameraPosition.zoom-1,
                0.0f, 0.0f),
                Animation(Animation.Type.LINEAR, 0.5F),
                null)
        }
        return binding.root


    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding?.map?.onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
        binding?.map?.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}




