import GoogleMapReact from 'google-map-react'
import {useEffect, useState} from "react";

const Map = ({coordinate, zoom, onClick = undefined, movable = false, }) => {
  const [key] = useState('AIzaSyDYIrGLUEv16eg26n7CtL9tipYAkefiSiM')
  const [coordinateInput, setCoordinate] = useState({lat:coordinate.lat, lng:coordinate.lng})
  const [inZoom, setZoom] = useState(zoom)

  console.log(coordinate)

  useEffect(() => {
    setCoordinate(coordinate)
  }, [coordinate])

  const handleOnClick = ({x, y, lat, lng, event}) => {
    if (!movable) {
      return
    }
    setCoordinate({lat: lat, lng: lng})
    if (onClick) {
      onClick(lat, lng)
    }
  }

  return (
    <div style={{height: '300px'}}>
      <GoogleMapReact bootstrapURLKeys={{key}} center={coordinateInput} defaultZoom={inZoom} onClick={handleOnClick}>
        <ReactMapPointComponent lat={coordinateInput.lat} lng={coordinateInput.lng}></ReactMapPointComponent>
      </GoogleMapReact>
    </div>
  )
}

const ReactMapPointComponent = () => {
  const markerStyle = {
    border: '1px solid white',
    borderRadius: '50%',
    width: 10,
    height: 10,
    backgroundColor: 'red',
    cursor: 'pointer',
    zInder: 10
  }
  return (
    <div style={markerStyle}></div>
  )
}

export default Map
