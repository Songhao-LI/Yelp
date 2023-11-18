import React from 'react'
import ReactDOM from 'react-dom/client'
import App from '../src/App'
import 'antd/dist/reset.css'
import axios from "axios"

axios.defaults.baseURL = 'http://127.0.0.1:8081'
const root = ReactDOM.createRoot(document.getElementById('root'))
root.render(<App />)
