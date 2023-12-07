import {useSearchParams} from "react-router-dom"
import {Row, Col, Divider, Rate, Carousel, Image, List, Typography, Button, Modal, Input, message} from "antd"
import {Content} from "antd/es/layout/layout";
import React,{useEffect, useState,useRef} from "react";
import Reservation from "../Reservation/Reservation";
import Map from "../Map";
import axios from "axios";
const  {Paragraph, Text} = Typography
const {TextArea} = Input


const Detail = ({windowHeight}) => {
  const [searchParams] = useSearchParams()
  const [detailInfo, setDetailInfo] = useState({
        "address": "",
        "comments": 0,
        "desc": "",
        "id": "1",
        "imgs": [],
        "lat": 0,
        "lng": 0,
        "star": 0,
        "time": "",
        "title": "",
        "username": ""
  })

  // 类似于computed
  useEffect(() => {
    // 有跨域问题，CORS，后端pip3 install diango-cors-headers, 配置setting 可以解决
    const currentID = searchParams.get('id')
    getDetailInfo(currentID)
  }, [])

  const getDetailInfo = (id) => {
    axios.get('/api/getDetails', {params: {id: id}}).then((res) => {
      if (res.data.code !== 0) {
        message.error(res.data.message)
        return
      }
      setDetailInfo(res.data.data)
    }).catch((error) => {
      message.error(error.message)
    })
  }

  return (
    <Content style={{minHeight: windowHeight}}>
      <Row style={{marginTop: 20}}>
        <Col span={2}></Col>
        <Col span={12}>
          <Description detail = {detailInfo}></Description>
          <Divider style={{color: 'grey'}} plain>Latest Comments</Divider>
          <Comments id={searchParams.get('id')}></Comments>
        </Col>
        <Col span={7} offset={1}>
          <Divider style={{color: 'grey'}} plain>Images</Divider>
          <Imgs imgData={detailInfo.imgs}></Imgs>
          <Divider style={{color: 'grey'}} plain>Address</Divider>
          <Map coordinate={{lat:detailInfo.lat, lng:detailInfo.lng}} zoom={6}></Map>
        </Col>
        <Col span={2}></Col>
      </Row>
    </Content>
  )
}

const Description = ({detail}) => {
  return (
    <div>
      <Row>
        <h1>{detail.title}</h1>
      </Row>
      <Row style={{marginTop: '10px', LineHeight:'35px', alignItems:'center'}}>
        <Col span={6}><Rate disabled defaultValue={detail.star} value={detail.star}></Rate></Col>
        <Col span={6}><span>Average: {detail.star} star</span></Col>
        <Col span={6}>Total: {detail.comments} comments</Col>
        <Col>Published on: {detail.time}</Col>
      </Row>
      <Row style={{marginTop: '20px'}}>
        <h3>Address: {detail.address}</h3>
      </Row>
      <Row style={{marginTop: '10px'}}>
        <h3>description:</h3>
      </Row>
      <Row style={{marginTop: '10px'}}>
        <span>{detail.desc}</span>
      </Row>
    </div>
  )
}

// 图片组件
const Imgs = ({imgData}) => {
  return (
    <div>
      <Carousel autoplay style={{ backgroundColor: `rgba(209, 209, 209, 0.55)`, height: 300, textAlign: 'center'}}>
        {
          imgData.map((img, idx) => {
            return (
              <Image key={idx} height={300} src={`http://127.0.0.1:8081/api/getImage?id=${img}`}></Image>
            )
          })
        }
      </Carousel>
    </div>
  )
}

const commentsData = [
]
const Comments = ({id}) => {
  const [comments, setComments] = useState(commentsData)
  const drawer= useRef(null);
  useEffect(() => {
    getCommentList(id)
  }, [comments])
  const commentsAddEventHandle = () => {
    // const data = commentsData.map(item => item)
    // setComments(data)
    getCommentList(id)
  }
  const getCommentList = (id) => {
    axios.get('/api/getCommentList', {params: {id: id}}).then((res) => {
      if (res.data.code !== 0) {
        message.error(res.data.message)
        return
      }
      if (res.data.data instanceof Array) {
        setComments(res.data.data)
      }
    }).catch((error) => {
      message.error(error.message)
    })
  }
  return (
    <div>
      <List header={<CommentButton id={id} addEventCallback={commentsAddEventHandle}></CommentButton>}
        bordered size='small' dataSource={comments} renderItem={(item) => (
          <List.Item>
            <Typography>
              <Paragraph>
                <span>username: {item.username}</span>
                <span style={{marginLeft:'20px'}}>Rate: {item.star}</span>
                <span style={{marginLeft:'20px'}}>Time: {item.time}</span>
              </Paragraph>
              <Text>{item.desc}</Text>
            </Typography>
          </List.Item>
        )}>
      </List>
      <Reservation ref={drawer}></Reservation>
    </div>
  )
}

const CommentButton = ({id, addEventCallback}) => {
  const [username, setUser] = useState('')
  const [desc, setDesc] = useState('')
  const [star, setStar] = useState(0)
  const [show, setShow] = useState(false)
  const handleShowModal = () => {
    setUser('')
    setStar(0)
    setDesc('')
    setShow(true)
  }
  const handleOKModal = () => {
    // 新增
    addCommentsToCurrent({
        'sourceId': id,
        'username': username,
        'desc': desc,
        'star': star
    })
    setShow(false)
  }
  const handleCancelModal = () => {
    setUser('')
    setStar(0)
    setDesc('')
    setShow(false)
  }
  const addCommentsToCurrent = (data) => {
    axios.post('/api/addComment', data, {
      header: {
        'Content-type': 'application/json'
      }
    }).then((res) => {
      if (res.data.code !== 0) {
        message.error(res.data.message)
        setShow(false)
        return
      }
      addEventCallback()
    }).catch((error) => {
      setShow(false)
      message.error(error.message)
    })
  }


  return (
    <div>
      <Button type='primary' size='small' onClick={handleShowModal}>Comment</Button>
      <Modal title='Reply' open={show} onOk={handleOKModal} onCancel={handleCancelModal}>
        <Row>
          <Col span={6}>username:</Col>
          <Col span={13}><Input size='small' value={username} onChange={e => {
            e.persist()
            setUser(e.target.value)
          }}></Input></Col>
        </Row>
        <Row style={{alignItems: 'center'}}>
          <Col span={6}>Rate:</Col>
          <Col span={13}><Rate value={star} onChange={setStar}></Rate></Col>
        </Row>
        <Row>
          <Col span={6}>Comments:</Col>
          <Col span={13}><TextArea row={4} value={desc} onChange={e => {
            e.persist()
            setDesc(e.target.value)
          }}></TextArea></Col>
        </Row>
      </Modal>
    </div>
  )
}


export default Detail