import React, {useState} from "react"
import {Button, Col, Input, Modal, Rate, Row, Upload, message} from "antd"
import {PlusOutlined} from '@ant-design/icons'
import Map from "../Map";
import axios from "axios";

const {TextArea} = Input

const getBase64 = (file) => new Promise((resolve, reject) => {
  const reader = new FileReader()
  reader.readAsDataURL(file)
  reader.onload = () => resolve(reader.result)
  reader.onerror = (error) => reject(error)
})

const HeadButton = ({newEventCallback}) => {
  // 定义输入
  const [user, setUser] = useState('')
  const [title, setTitle] = useState('')
  const [address, setAddress] = useState('')
  const [desc, setDesc] = useState('')
  const [star, setStar] = useState(0)
  const [coordinate, setCoordinate] = useState({lat: 40.729675, lng: -73.99692})
  const handleMapClick = (lat, lng) => {
    setCoordinate({lat: lat, lng: lng})
  }
  // 展示
  const [imagePreviewShow, setImagePreviewShow] = useState(false)
  const [show, setShow] = useState(false)
  const [version, setVersion] = useState(0)
  // 上传预览
  const [imagePreviewTitle, setImagePreviewTitle] = useState('')
  const [imageURL, setImageURL] = useState('')
  // 控制最大数量
  const [maxUploads] = useState(6)
  const [uploads, setUploads] = useState([])

  // 控制显示隐藏方法
  const handleShowModal = () => {
    setUser('')
    setTitle('')
    setAddress('')
    setStar(0)
    setCoordinate({lat: 40.729675, lng: -73.99692})
    setShow(true)
    setUploads([])
  }

  const submit = (param) => {
    axios.post('api/addToList', param, {
      headers: {
        "Content-Type": "application/json"
      }
    }).then((res) => {
      if (res.data.code !== 0) {
        message.error(res.data.message)
        return
      }
      message.success('Submit success')
      newEventCallback(version + 1)
      setVersion(version)
    }).catch((error) => {
      message.error(error.message)
    })
  }
  const handleOnOK = () => {
    submit({
        "title": title,
        "username": user,
        "star": star,
        "lat": coordinate.lat,
        "lng": coordinate.lng,
        "address": address,
        "desc": desc,
        "imgs": uploads.map(item => item.response.data.id)
    })
    setShow(false)
  }
  const handleOnCancel = () => {
    setShow(false)
  }


  // 预览预处理
  const preventBUG = () => {
    return false
  }
  // 预览
  const previewImageHandler = async (file) => {
    if (!file.url && !file.preview) {
      file.preview = await getBase64(file.originFileObj)
    }
    setImageURL(file.preview || file.url)
    setImagePreviewTitle(file.name || file.url.substring(file.url.lastIndexOf('/') + 1))
    setImagePreviewShow(true)
  }
  // 取消显示
  const imagePreviewShowCancel = () => {
    setImagePreviewShow(false)
  }
  // 控制最大数量
  const uploadToListHandler = ({file, fileList, event}) => {
    setUploads(fileList)
    if (file.status === 'uploading') {
      message.success('uploading, please wait')
      setUploads(fileList)
    }
    if (file.status === 'done') {
      message.success('upload success')
      setUploads(fileList)
    }
    if (file.status === 'error') {
      message.error('upload fail, please re-try')
      const list = fileList.filter(item => item.uid !== file.uid)
      setUploads(list)
    }
  }
  // 按钮组件
  const UploadButton = (
      <div>
        <PlusOutlined></PlusOutlined>
        <div style={{marginLeft: 5}}>Upload</div>
      </div>
  )

  return (
    <div style={{
      float: 'right',
      display: 'block',
      width: '100px'
    }}>
      <Button id={'newBtn'} style={{
        backgroundColor: 'transparent',
        color: 'white',
      }} size = 'large' onClick={handleShowModal}>New</Button>
      <Modal width={'800px'} title='Share' open={show} onOk={handleOnOK} onCancel={handleOnCancel}>
        <Row><Col span={5}>username:</Col></Row>
        <Row><Col span={24}>
          <Input id={'username'} size='small' value={user} onChange={e => {
            e.persist()
            setUser(e.target.value)
          }}></Input>
        </Col></Row>

        <Row><Col span={5}>title:</Col></Row>
        <Row><Col span={24}>
          <Input id={'title'} size='small' value={title} onChange={e => {
            e.persist()
            setTitle(e.target.value)
          }}></Input>
        </Col></Row>

        <Row><Col span={5}>Rate:</Col></Row>
        <Row><Col span={24}>
          <Rate onChange={setStar}></Rate>
        </Col></Row>

        <Row><Col span={5}>address:</Col></Row>
        <Row><Col span={24}>
          <Input id={'address'} size='small' value={address} onChange={e => {
            e.persist()
            setAddress(e.target.value)
          }}></Input>
        </Col></Row>

        <Row><Col span={5}>position info:</Col></Row>
        <Row><Col span={24}><Map coordinate={{lat: coordinate.lat, lng: coordinate.lng}} zoom={20} onClick={handleMapClick} movable={true}></Map></Col></Row>

        <Row><Col span={5}>pictures:</Col></Row>
        <Row><Col span={24}>
          <Upload action = "http://127.0.0.1:8081/api/upload" listType='picture-card' fileList={uploads}
                  onPreview={previewImageHandler}
                  onChange={uploadToListHandler}
          >
            {uploads.length >= maxUploads ? null : UploadButton}
          </Upload>
        </Col></Row>

        <Row><Col span={5}>description:</Col></Row>
        <Row><Col span={24}><TextArea id={'description'} row={4} onChange={e => {
            e.persist()
            setDesc(e.target.value)
          }}></TextArea></Col></Row>
      </Modal>
      <Modal open={imagePreviewShow} title={imagePreviewTitle} footer={null} onCancel={imagePreviewShowCancel}>
        <img alt='pic' style={{width: '100%'}} src={imageURL}></img>
      </Modal>
    </div>
  )
}

export default HeadButton
