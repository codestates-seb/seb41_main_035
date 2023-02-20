import styled from 'styled-components';
import axios from 'axios';
import { CloseOutlined, LeftSquareFilled } from '@ant-design/icons';
import { useState } from 'react';
import Logo from '../../svg/Logo.svg';

const API_URL = process.env.REACT_APP_API_URL;
function Signup(props) {
  const [id, setId] = useState('');
  const [password, setPassword] = useState('');
  const [nickname, setNickname] = useState('');
  const [height, setHeight] = useState('');
  const [weight, setWeight] = useState('');

  const validationCheck = (e, value) => {
    if (value === 'nickname') {
      if (!e.length) {
        window.alert('닉네임을 입력해주세요');
        return false;
      }
    }

    if (value === 'email') {
      const emailRegex =
        /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
      if (!emailRegex.test(e)) {
        window.alert('올바른 이메일 형식이 아닙니다');
        return false;
      }
    }
    if (value === 'password') {
      const passwordRegex =
        /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/;
      if (!passwordRegex.test(e)) {
        window.alert('숫자,영문,특수문자를 포함해 8자리 이상이어야 합니다');
        return false;
      }
    }
    return true;
  };
  const closeButton = () => {
    // eslint-disable-next-line react/prop-types
    props.onClose();
  };

  const onChangeId = (e) => {
    setId(e.target.value);
  };

  const onChangePassword = (e) => {
    setPassword(e.target.value);
  };

  const onChangeNickname = (e) => {
    setNickname(e.target.value);
  };

  const onChangeHeight = (e) => {
    setHeight(e.target.value);
  };

  const onChangeWeight = (e) => {
    setWeight(e.target.value);
  };

  const SignUpUser = async () => {
    if (
      validationCheck(nickname, 'nickname') &&
      validationCheck(id, 'email') &&
      validationCheck(password, 'password')
    ) {
      const res = await axios.post(
        `${API_URL}members/signup`,
        {
          nickname: nickname,
          email: id,
          password: password,
          height: height,
          weight: weight,
        },
        { withCredentials: true }
      );
      if (res) {
        // eslint-disable-next-line react/prop-types
        props.onClose();
      }
    }
  };

  return (
    <Overlay>
      <ModalWrap>
        <CloseOutlined
          style={{ 'margin-left': '85%', 'margin-top': '5%' }}
          onClick={closeButton}
        />
        <Contents>
          <h1
            style={{
              'margin-bottom': '5%',
              display: 'flex',
              'justify-content': 'space-evenly',
              fontSize: '30px',
              position: 'relative',
            }}
          >
            <img src={Logo} alt="logo" className="title" role="presentation" />
          </h1>
          <h2 style={{ fontSize: '20px', position: 'relative' }}>회원가입</h2>
          <SInput
            className="id"
            placeholder="아이디"
            onChange={onChangeId}
          ></SInput>
          <SInput
            className="pw"
            placeholder="비밀번호"
            onChange={onChangePassword}
          ></SInput>

          <SInput
            className="na"
            placeholder="닉네임"
            onChange={onChangeNickname}
          ></SInput>
          <SInput
            className="ta"
            placeholder="키"
            onChange={onChangeHeight}
          ></SInput>
          <SInput
            className="wg"
            placeholder="몸무게"
            onChange={onChangeWeight}
          ></SInput>
          <SigninButton onClick={SignUpUser}>회원가입</SigninButton>
        </Contents>
      </ModalWrap>
    </Overlay>
  );
}

const Overlay = styled.div`
  position: fixed;
  width: 100%;
  height: 100%;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.2);
  z-index: 9999;
  text-align: center;
`;

const ModalWrap = styled.div`
  width: 400px;
  height: 500px;
  border-radius: 15px;
  background-color: #fff;
  position: absolute;
  top: 40%;
  left: 50%;
  transform: translate(-50%, -50%);
`;

const Contents = styled.div`
  margin: 50px 30px;
  h1 {
    font-size: 30px;
    font-weight: 600;
    margin-bottom: 60px;
  }
  img {
    margin-top: -60px;
    width: 300px;
  }
`;

const SigninButton = styled.button`
  font-size: 14px;
  background-color: #4083b1;
  border-radius: 8px;
  color: white;
  font-style: italic;
  font-weight: 200;
  cursor: pointer;
  &:hover {
    background-color: #67b8f0;
  }
  text-align: center;
  margin-top: 20px;
  border: none;
  width: 280px;
  height: 44px;
`;
const SInput = styled.input`
  border: 1.5px solid #a1a1a1;
  font-size: 14px;
  padding: 12px 12px;
  background-color: gary;
  border-radius: 8px;
  width: 250px;
  color: black;
  font-weight: 200;
  margin-top: -30px;
  position: relative;
  height: 5px;
  margin-bottom: 10px;
  .title {
    width: 260px;
  }
`;

export default Signup;
