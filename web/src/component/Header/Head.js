import React, {useState} from 'react'
import {Layout, Menu} from "antd"
import HeadButton from "./HeadButton";
import {useNavigate} from "react-router-dom";
import '../index.css'
const {Header} = Layout


const Head = ({newEventCallback}) => {
  const [menus, setMenus] = useState([
    {title: 'Playground', path: '/'},
    {title: 'About', path: '/about'},
  ])
  const navigate = useNavigate()
  const menuClick = (event) => {
    navigate(event.item.props.path)
  }
  // const [headWidth, setHeadWidth] = useState(window.innerWidth)

  return (
    <Header style = {{
      backgroundColor: 'rgb(220, 54, 70)'
    }}>
      <div style={{
        color: 'white',
        fontSize: '22px',
        float: 'left',
        width: '120px',
        display: 'block'
      }}>
        Yelp
      </div>

      <div style={{
        marginLeft: '50px',
        width: '200px',
        float: 'left',
        display: 'block'
      }}>
        <Menu style={{
          backgroundColor: 'transparent',
          fontSize: '16px',
          color: 'rgba(255, 255, 255, 0.55)'
        }}
        mode='horizontal'
        defaultSelectedKeys = {['Playground']}
        items = {menus.map((item) => {
          const key = item.title
          return {key, label: `${item.title}`, path: item.path}
        })}
        onClick={menuClick}></Menu>
      </div>

      <HeadButton newEventCallback={newEventCallback}></HeadButton>
    </Header>
  )
}
export default Head