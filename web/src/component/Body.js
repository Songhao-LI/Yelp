import React, {useEffect, useState} from 'react'
import {Content} from "antd/es/layout/layout"
import {Link} from "react-router-dom"
import {List, Card, Rate, message} from "antd"
import axios from "axios";

const { Meta } = Card

const Body = ({windowHeight, newEventNotice}) => {
  return (
      <Content style = {{
        minHeight: windowHeight
      }}>
        <Parks newEventNotice={newEventNotice}></Parks>
      </Content>
  )
}

// detail
const Parks = ({newEventNotice}) => {
  const [listData, setListData] = useState([])
  // 类似于computed
  useEffect(() => {
    // 有跨域问题，CORS，后端pip3 install diango-cors-headers, 配置setting 可以解决
    getListData()
  }, [newEventNotice])

  // 获取首页数据
  const getListData = () => {
    axios.get('/api/getList', {params: {}}).then((res) => {
      if (res.data.code !== 0) {
        message.error(res.data.message)
        return
      }
      if (res.data.data instanceof Array) {
        setListData(res.data.data)
      }
    }).catch((error) => {
      message.error(error.message)
    })
  }

  return (
    <div style={{marginLeft:'35px', marginTop: '20px'}}>
      <List grid={{column:4}} dataSource={listData} renderItem={(item) => (
        <List.Item>
          <Link target='_blank' to={{ pathname:`/detail`, search:`id=${item.id}` }}>
            <Card style={{width: 300}} cover={<img style={{height:180, width: 300}} src={`http://127.0.0.1:8081/api/getImage?id=${item.imgs[0]}`}/>}>
              <Rate disabled defaultValue = {item.star}></Rate>
              <Meta title={item.title} description={ `${item.desc.substring(0,16)}...`}></Meta>
            </Card>
          </Link>
        </List.Item>
      )}>
      </List>
    </div>
  )
}

export default Body
