import styled from 'styled-components';
import Sidebar from '../components/Sidebar';
import PostBox from '../components/PostBox';
import { AiFillCaretDown } from 'react-icons/ai';
const Outer = () => {
  const onDpMenu = () => {
    let click = document.getElementById('drop-content');
    if (click.style.display === 'none') {
      click.style.display = 'flex';
    } else {
      click.style.display = 'none';
    }
  };
  return (
    <>
      <SWrapper>
        <Sidebar />
        <div className="main post">
          <span className="category-name">아우터</span>
          <Filter>
            <div className="rental availability ">
              <input type="checkbox" name="rental" value="" />
              렌탈 가능
            </div>
            <div className="dropdown">
              <button className="dropdown_button" onClick={onDpMenu}>
                <AiFillCaretDown />
                정렬 순서
              </button>
              <div id="drop-content">
                <button>가격 낮은순</button>
                <button>가격 높은순</button>
                <button>최신순</button>
              </div>
            </div>
          </Filter>
          <PostBox />
        </div>
      </SWrapper>
    </>
  );
};
const SWrapper = styled.div`
  display: flex;
  .post {
    display: flex;
    flex-direction: column;
  }
  .category-name {
    text-align: center;
    font-size: 50px;
    margin-top: 50px;
  }
`;
const Filter = styled.div`
  display: flex;
  justify-content: space-around;
  .availability {
    margin-top: 60px;
    margin-left: -50px;
  }
  .dropdown_button {
    margin-top: 40px;
    font-size: 20px;
    width: 120px;
    height: 35px;
    position: releative;
  }
  #drop-content {
    display: flex;
    display: none;
    width: 120px;
    flex-direction: column;
    position: absolute;
    z-index: 999;
    button {
      background-color: white;
      border: 0.1px solid gray;
      cursor: pointer;
    }
  }
`;
export default Outer;
