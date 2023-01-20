import styled from 'styled-components';
import PostBox from '../components/PostBox';
import { BiCaretDownCircle } from 'react-icons/bi';
import { useParams } from 'react-router-dom';
import CATEGORY_CODE from '../constants/index';
import { useState, useEffect } from 'react';
import axios from 'axios';
import CategoryData from '../db/CategoryData.json';
const Category = () => {
  const params = useParams();
  const category = params.categoryId;
  const [data, setData] = useState([]);
  const onDpMenu = () => {
    let click = document.getElementById('drop-content');
    if (click.style.display === 'none') {
      click.style.display = 'flex';
    } else {
      click.style.display = 'none';
    }
  };
  const postCate = (url) => {
    axios
      .get(url)
      .then((res) => {
        setData([...res.data]);
        console.log(res.data);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  useEffect(() => {
    window.scrollTo(0, 0);
    if (category === 'outer') {
      // postCate();
      setData(CategoryData.outer);
      console.log(CategoryData.outer);
    } else if (category === 'top') {
      // postCate();
      setData(CategoryData.top);
      console.log(CategoryData.top);
    } else if (category === 'bottom') {
      postCate();
    } else if (category === 'onepiece') {
      postCate();
    } else if (category === 'hat') {
      postCate();
    } else if (category === 'shoes') {
      postCate();
    }
  }, [category]);
  return (
    <SWrapper>
      <div className="bottom">
        <div className="main post">
          <span className="category-name">{CATEGORY_CODE[category]}</span>
          <Filter>
            <div className="rental availability ">
              <input type="checkbox" name="rental" value="" />
              렌탈 가능
            </div>
            <div className="dropdown">
              <button className="dropdown_button" onClick={onDpMenu}>
                <BiCaretDownCircle />
                정렬 순서
              </button>
              <div id="drop-content">
                <button>가격 낮은순</button>
                <button>가격 높은순</button>
                <button>최신순</button>
              </div>
            </div>
          </Filter>
          <PostBox data={data} />
        </div>
      </div>
    </SWrapper>
  );
};
const SWrapper = styled.div`
  display: flex;
  justify-content: flex-end;
  .bottom {
    width: 75%;
  }
  .post {
    width: 80%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
  .category-name {
    text-align: center;
    font-size: 50px;
    margin-top: 50px;
  }
`;
const Filter = styled.div`
  display: flex;
  width: 105%;
  justify-content: space-between;
  .availability {
    margin-top: 50px;
    margin-bottom: 10px;
  }
  .dropdown_button {
    margin-top: 40px;
    font-size: 20px;
    width: 120px;
    height: 35px;
    position: releative;
    border: none;
    display: flex;
    align-items: center;
    cursor: pointer;
  }
  #drop-content {
    display: flex;
    display: none;
    width: 120px;
    flex-direction: column;
    position: absolute;
    z-index: 999;
    button {
      height: 30px;
      border: 0.1px solid gray;
      cursor: pointer;
      background-color: #eee5ca;
    }
  }
`;
export default Category;
