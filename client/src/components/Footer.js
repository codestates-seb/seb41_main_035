import styled from 'styled-components';
import { BsGithub, BsEmojiSmile } from 'react-icons/bs';

const GITHUB_URL = 'https://github.com';
//코드반복화
const Writer = [
  {
    name: '김나율',
    role: 'Frontend',
    github: `${GITHUB_URL}/nayul34`,
  },
  {
    name: '박영선',
    role: 'Frontend',
    github: `${GITHUB_URL}/yspark14`,
  },
  {
    name: '이승준',
    role: 'Frontend',
    github: `${GITHUB_URL}/01055986186`,
  },
  {
    name: '이도형',
    role: 'Backend',
    github: `${GITHUB_URL}/eheh12321`,
  },
  {
    name: '오성범',
    role: 'Backend',
    github: `${GITHUB_URL}/Seongbaem`,
  },
  {
    name: '조혜주',
    role: 'Backend',
    github: `${GITHUB_URL}/codestates-seb/seb41_main_035`,
  },
  // {
  //   name: '뽐내조',
  //   role: 'MainProject-035',
  //   github: `${GITHUB_URL}/hyejuc`,
  // },
];

const Footer = () => {
  return (
    <FWrapper>
      <div className="team-name">
        <a href="https://github.com/codestates-seb/seb41_main_035">
          MainProject-035 | 뽐내조
        </a>
      </div>

      <Scontainer>
        {Writer.map((item, index) => {
          return (
            <div key={index} className="FooterBox">
              <a href={item.github}>
                <BsGithub className="github" />
                <span>{item.role}</span>
                <span>{item.name}</span>
              </a>
            </div>
          );
        })}
        {/* 
        <div className="FooterInfo">
          <a href="https://github.com/codestates-seb/seb41_main_035">
            <BsEmojiSmile className="github" />
            MainProject-035
            <span> 뽐내조</span>
          </a>
        </div> */}
      </Scontainer>
      <div className="copyright">
        Copyright 2023. Look at me inc. all rights reserved.
      </div>
    </FWrapper>
  );
};
const FWrapper = styled.div`
  text-align: center;
  border-top: 3px solid #196ba5;
  .team-name {
    font-size: 18px;
    margin-top: 10px;
    font-weight: 700;
    a {
      text-decoration: none;
      color: black;
    }
  }

  .copyright {
    color: #9e9e9e;
    font-size: 13px;
    margin-bottom: 10px;
  }
`;
const Scontainer = styled.div`
  display: flex;
  justify-content: space-evenly;

  /* justify-content: center; */
  /* 중앙정렬 */
  /* margin: 0 auto; */
  width: 100%;
  height: 9vh;
  padding-top: 35px;

  .FooterBox {
    cursor: pointer;
  }
  .github {
    font-size: 30px;
    display: flex;
    margin: 0 auto;
  }
  span {
    display: flex;
    justify-content: center;
  }
  a {
    text-decoration: none;
    color: #000000;
    font-weight: 500;
    font-size: 14px;
  }
`;

export default Footer;
